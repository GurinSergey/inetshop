export class FieldEntry {
  id: number;
  name: string;
  value: string;

  constructor(id: number, name: string, value: string) {
    this.id = id;
    this.name = name;
    this.value = value;
  }
}

export class FieldList {
  list: FieldEntry[];

  constructor(list: FieldEntry[] = []) {
    this.list = list;
  }
}
