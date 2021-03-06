const path = require('path');
const webpack = require('webpack');

/**
 * This is a server config which should be merged on top of common config
 */
module.exports = {
  mode: 'development',
  externals: [/(node_modules|main\..*\.js)/],
  entry: {
    // This is our Express server for Dynamic universal
    server: './server.ts',
    // This is an example of Static prerendering (generative)
    prerender: './prerender.ts'
  },
  resolve: { extensions: [".js", ".ts"] },
  output: {
    // Puts the output at the root of the dist folder
    path: path.join(__dirname),
    filename: '[name].js'
  },

  plugins: [
    new webpack.ContextReplacementPlugin(
      // fixes WARNING Critical dependency: the request of a dependency is an expression
      /(.+)?angular(\\|\/)core(.+)?/,
      path.join(__dirname, 'src'), // location of your src
      {} // a map of your routes
    ),
    new webpack.ContextReplacementPlugin(
      // fixes WARNING Critical dependency: the request of a dependency is an expression
      /(.+)?express(\\|\/)(.+)?/,
      path.join(__dirname, 'src'),
      {}
    )
  ],
  target: 'node',
  node: {
    __dirname: false
  },
};

// Work around for https://github.com/angular/angular-cli/issues/7200
//
// const path = require('path');
// const webpack = require('webpack');
//
// module.exports = {
//   entry: {
//     server: './server.ts',
//     prerender: './prerender.ts'
//   },
//   target: 'node',
//   resolve: { extensions: ['.ts', '.js'] },
//   externals: [/(node_modules|main\..*\.js)/,],
//   output: {
//     libraryTarget: 'commonjs2',
//     path: path.join(__dirname,'dist'),
//     filename: '[name].js'
//   },
//   module: {
//     rules: [
//       { test: /\.ts$/, loader: 'ts-loader' }
//     ]
//   },
//   optimization: {
//     minimize: false
//   },
//   plugins: [
//     new webpack.ContextReplacementPlugin(
//       // fixes WARNING Critical dependency: the request of a dependency is an expression
//       /(.+)?angular(\\|\/)core(.+)?/,
//       path.join(__dirname, 'src'), // location of your src
//       {} // a map of your routes
//     ),
//     new webpack.ContextReplacementPlugin(
//       // fixes WARNING Critical dependency: the request of a dependency is an expression
//       /(.+)?express(\\|\/)(.+)?/,
//       path.join(__dirname, 'src'),
//       {}
//     )
//   ]
// }
