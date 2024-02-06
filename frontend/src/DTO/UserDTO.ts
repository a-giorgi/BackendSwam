export default interface UserDTO{
    id: number | null;
    username: string;
    email: string;
    password: string;
    oldPassword: string;
    confirmPassword: string;
    countryCode: string;
  }