import 'zone.js/dist/zone-node';
import 'reflect-metadata';
/*How to start
yarn or npm install
yarn start or npm run start - for client rendering
yarn ssr or npm run ssr - for server-side rendering
yarn build:universal or npm run build:universal - for assembly in release
yarn server or npm run server - to start the server
yarn build:prerender or npm run build:prerender - to generate static by static.paths.ts
for watch with ssr:
npm run ssr:cw - build client
npm run ssr:sw - build server
npm run ssr:webpack -build server.js
npm run ssr:server - server with watch*/

const domino = require('domino');
const fs = require('fs');
const path = require('path');
const template = fs.readFileSync(path.join(__dirname, '.', 'dist', 'index.html')).toString();
const win = domino.createWindow(template);
const files = fs.readdirSync(`${process.cwd()}/dist-server`);

global['window'] = win;
Object.defineProperty(win.document.body.style, 'transform', {
  value: () => {
    return {
      enumerable: true,
      configurable: true
    };
  },
});
global['document'] = win.document;
global['localStorage'] = win.localStorage;
global['CSS'] = null;
// global['XMLHttpRequest'] = require('xmlhttprequest').XMLHttpRequest;
global['Prism'] = null;

import 'reflect-metadata';
import 'zone.js/dist/zone-node';
import { enableProdMode } from '@angular/core';
import * as express from 'express';
import * as compression from 'compression';
import * as cookieparser from 'cookie-parser';
const { provideModuleMap } = require('@nguniversal/module-map-ngfactory-loader');

const mainFiles = files.filter(file => file.startsWith('main'));
const hash = mainFiles[0].split('.')[1];
const { AppServerModuleNgFactory, LAZY_MODULE_MAP } = require(`./dist-server/main.${hash}`);
import { ngExpressEngine } from '@nguniversal/express-engine';
import { REQUEST, RESPONSE } from '@nguniversal/express-engine/tokens';
import {ROUTES} from "./static.paths";
const PORT = process.env.PORT || 8065;


enableProdMode();

const app = express();
app.use(compression());
app.use(cookieparser());

const redirectowww = false;
const redirectohttps = true;
const wwwredirecto = true;
/*app.use((req, res, next) => {
    // for domain/index.html
    if (req.url === '/index.html') {
      res.redirect(301, 'https://' + req.hostname);
    }

    // check if it is a secure (https) request
    // if not redirect to the equivalent https url
    if (redirectohttps && req.headers['x-forwarded-proto'] !== 'https' && req.hostname !== 'localhost') {
      // special for robots.txt
      if (req.url === '/robots.txt') {
        next();
        return;
      }
      res.redirect(301, 'https://' + req.hostname + req.url);
    }

    // www or not
    if (redirectowww && !req.hostname.startsWith('www.')) {
      res.redirect(301, 'https://www.' + req.hostname + req.url);
    }

    // www or not
    if (wwwredirecto && req.hostname.startsWith('www.')) {
      const host = req.hostname.slice(4, req.hostname.length);
      res.redirect(301, 'https://' + host + req.url);
    }

    next();
  }
);
*/
app.engine('html', ngExpressEngine({
  bootstrap: AppServerModuleNgFactory,
  providers: [
    provideModuleMap(LAZY_MODULE_MAP)
  ]
}));

app.set('view engine', 'html');
app.set('views', 'src');

app.use(express.static(__dirname + '/assets', { index: false }));

app.get('*.*', express.static(path.join(__dirname, '.', 'dist')));
app.get(ROUTES, express.static(path.join(__dirname, '.', 'static')));

app.get('*', (req, res) => {
  global['navigator'] = req['headers']['user-agent'];
  const http = req.headers['x-forwarded-proto'] === undefined ? 'http' : req.headers['x-forwarded-proto'];

  const url = req.originalUrl;
  // tslint:disable-next-line:no-console
  console.log(`GET: ${url}`);
  //'../dist/index',
  res.render(
    '../dist/index',
    {
      req: req,
      res: res,
      providers: [
        {
          provide: REQUEST, useValue: (req)
        },
        {
          provide: RESPONSE, useValue: (res)
        },
        {
          provide: 'ORIGIN_URL',
          useValue: (`${http}://${req.headers.host}`)
        }
      ]
    },
    (err, html) => {
      if (!!err) { throw err; }

      // tslint:disable-next-line:no-console
    //  console.timeEnd(`GET: ${url}`);
      console.log('GET:' + url);
      res.send(html);
    });
});

app.listen(PORT, () => {
  console.log(`listening on http://localhost:${PORT}!`);
});
