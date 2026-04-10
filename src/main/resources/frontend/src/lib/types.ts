export type GenderType = "MALE" | "FEMALE";
export type RoleType = "ADMIN" | "USER";
export type EmailVerificationStatus = "UNVERIFIED" | "CREATED" | "PROCESSING" | "SENT" | "VERIFIED";

export interface ApiErrorResponse {
  status: number;
  error: string;
  message: string;
  timestamp: string;
}

export interface LoginRequestDto {
  email: string;
  password: string;
}

export interface JwtAuthenticationResponse {
  accessToken: string;
}

export interface UserRequestDto {
  firstName: string;
  lastName: string;
  birthDate: string;
  gender: GenderType;
  email: string;
  password: string;
  userConsent: boolean;
  privacyConsent: boolean;
}

export interface UserDto {
  id: string;
  firstName: string;
  lastName: string;
  birthDate: string;
  gender: GenderType;
  roleId: string;
  roleName: RoleType;
  roleDescription: string;
  email: string;
  emailVerified: boolean;
  userConsent: boolean;
  privacyConsent: boolean;
  createdBy?: string;
  createdDate?: string;
  modifiedBy?: string;
  modifiedDate?: string;
}

export interface MedicalDto {
  id: string;
  countryEn: string;
  countryRu: string;
  type: string;
  name: string;
  activeIngredient: string;
  description: string;
  indications: string;
  contraindications: string;
  dosing: string;
  kidneyFriendly: boolean;
  pregnantFriendly: boolean;
  breastfedFriendly: boolean;
  liverFriendly: boolean;
  childFriendly: boolean;
  stomachFriendly: boolean;
}

export interface UserHealthProfileRequestDto {
  weight?: number;
  chronicConditions: string[];
  healthFeatures: string[];
  allergies: string[];
}

export interface UserHealthProfileDto {
  id: string;
  userId: string;
  weight?: number;
  chronicConditions: string[];
  healthFeatures: string[];
  allergies: string[];
  createdBy?: string;
  createdDate?: string;
  modifiedBy?: string;
  modifiedDate?: string;
}

export interface EmailVerificationDto {
  status: EmailVerificationStatus;
}

export interface ResetPasswordRequestDto {
  oldPassword: string;
  newPassword: string;
}

export interface MedicalFilters {
  countryEn?: string;
  category?: string;
  name?: string;
}

export interface JwtPayload {
  sub?: string;
  exp?: number;
  iat?: number;
  [key: string]: unknown;
}
