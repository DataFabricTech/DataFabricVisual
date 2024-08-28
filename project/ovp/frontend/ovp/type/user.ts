export interface AddUser {
  email: string;
  name: string;
  displayName: string;
  description: string;
  password: string;
  confirmPassword: string;
  isAdmin: boolean;
  isBot: boolean;
  createPasswordType: string;
}
