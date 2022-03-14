export class TemplateEntry{
  id: number;
  name: string;

  constructor(id: number, name: string) {
    this.id = id;
    this.name = name;
  }
}

export class CatalogEntry {
  id: number;
  name: string;
  parent_id: number;
  photo_path: string;
  template: TemplateEntry;
  children: CatalogEntry[] = [];

  constructor(id: number = 0, name: string = '', parent_id: number = 0) {
    this.id = id;
    this.name = name;
    this.parent_id = parent_id;
  }
}
