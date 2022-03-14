import {GlobalConst} from '../globals/GlobalConst';

export class SliderEntry {
  latIdent: string;
  title: string;
  description: string;
  code: string;
  url_image: string;

  constructor(latIdent, title, description, code, url_image = GlobalConst.SERVER_IMG_ADDR + 'images/no-image.png') {
    this.latIdent = latIdent;
    this.title = title;
    this.description = description;
    this.code = code;
    this.url_image = url_image;
  }
}
