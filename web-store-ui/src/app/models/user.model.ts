export class User {
  id: number;
  userName:  string;
  personName: string;
  mailConfirm:boolean|string;
  birthdate:string;
  sex:string;
  state: string;
  userRoles: UserRole[]=[];



  constructor(id: number, userName: string, personName: string, mailConfirm: boolean, birthdate: string, sex: string, state: string, authRoles: UserRole[]) {
    this.id = id;
    this.userName = userName;
    this.personName = personName;
    this.mailConfirm = mailConfirm;
    this.birthdate = birthdate;
    this.sex = sex;
    this.state = state;

    for (let authRole of authRoles) {
      this.userRoles.push(new UserRole(authRole.authority));
    }

  }
}

export class UserRole {
  roleName:string;
  authority:string;
  constructor(authority: string) {
    this.roleName = authority;
  }
}

export const states = ['ACTIVE', 'BLOCKED', 'INACTIVE'];
export const roles = ['ROLE_USER', 'ROLE_ADMIN'];
