import type {
  ApiErrorResponse,
  EmailVerificationDto,
  JwtAuthenticationResponse,
  LoginRequestDto,
  MedicalDto,
  MedicalFilters,
  ResetPasswordRequestDto,
  UserDto,
  UserHealthProfileDto,
  UserHealthProfileRequestDto,
  UserRequestDto,
} from "@/lib/types";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8090";

export class ApiError extends Error {
  status: number;
  details?: ApiErrorResponse | string | null;

  constructor(message: string, status: number, details?: ApiErrorResponse | string | null) {
    super(message);
    this.name = "ApiError";
    this.status = status;
    this.details = details;
  }
}

export function getApiBaseUrl(): string {
  return API_BASE_URL;
}

async function parseBody(response: Response): Promise<unknown> {
  const text = await response.text();
  if (!text) return null;

  try {
    return JSON.parse(text) as unknown;
  } catch {
    return text;
  }
}

function createQuery(params: Record<string, string | number | boolean | undefined>): string {
  const searchParams = new URLSearchParams();

  Object.entries(params).forEach(([key, value]) => {
    if (value === undefined || value === "") return;
    searchParams.set(key, String(value));
  });

  const result = searchParams.toString();
  return result ? `?${result}` : "";
}

export async function request<T>(
  path: string,
  options: RequestInit = {},
  token?: string | null,
): Promise<T> {
  const headers = new Headers(options.headers);
  headers.set("Accept", "application/json");

  if (options.body && !headers.has("Content-Type")) {
    headers.set("Content-Type", "application/json");
  }

  if (token) {
    headers.set("Authorization", `Bearer ${token}`);
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers,
  });

  if (!response.ok) {
    const body = (await parseBody(response)) as ApiErrorResponse | string | null;
    let message = response.statusText;

    if (typeof body === "string") {
      message = body;
    } else if (body && typeof body === "object" && "message" in body) {
      message = String(body.message);
    }

    throw new ApiError(message || "Ошибка запроса", response.status, body);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  const body = await parseBody(response);
  return body as T;
}

export const api = {
  auth: {
    login(payload: LoginRequestDto) {
      return request<JwtAuthenticationResponse>("/api/login", {
        method: "POST",
        body: JSON.stringify(payload),
      });
    },
    logout(token?: string | null) {
      return request<void>("/api/logout", { method: "POST" }, token);
    },
  },
  medicals: {
    list(filters: MedicalFilters, token?: string | null) {
      return request<MedicalDto[]>(
        `/api/medicals${createQuery({
          countryEn: filters.countryEn,
          category: filters.category,
          name: filters.name,
        })}`,
        { method: "GET" },
        token,
      );
    },
    getById(id: string, token?: string | null) {
      return request<MedicalDto>(`/api/medicals/${id}`, { method: "GET" }, token);
    },
    getCategories(token?: string | null) {
      return request<string[]>("/api/medicals/categories", { method: "GET" }, token);
    },
    getCountries(translateCountry = false, token?: string | null) {
      return request<string[]>(
        `/api/medicals/countries${createQuery({ translateCountry })}`,
        { method: "GET" },
        token,
      );
    },
    getNames(token?: string | null) {
      return request<string[]>("/api/medicals/names", { method: "GET" }, token);
    },
  },
  users: {
    list(token?: string | null) {
      return request<UserDto[]>("/api/users", { method: "GET" }, token);
    },
    getById(id: string, token?: string | null) {
      return request<UserDto>(`/api/users/${id}`, { method: "GET" }, token);
    },
    create(payload: UserRequestDto) {
      return request<UserDto>("/api/users", {
        method: "POST",
        body: JSON.stringify(payload),
      });
    },
    update(id: string, payload: UserRequestDto, token?: string | null) {
      return request<UserDto>(`/api/users/${id}`, {
        method: "POST",
        body: JSON.stringify(payload),
      }, token);
    },
    remove(id: string, token?: string | null) {
      return request<UserDto>(`/api/users/${id}`, { method: "DELETE" }, token);
    },
    getMedicalViews(id: string, token?: string | null) {
      return request<MedicalDto[]>(`/api/users/${id}/views`, { method: "GET" }, token);
    },
    clearMedicalViews(id: string, token?: string | null) {
      return request<void>(`/api/users/${id}/views`, { method: "DELETE" }, token);
    },
    createHealthProfile(id: string, payload: UserHealthProfileRequestDto, token?: string | null) {
      return request<UserHealthProfileDto>(`/api/users/${id}/health`, {
        method: "POST",
        body: JSON.stringify(payload),
      }, token);
    },
    getHealthProfile(id: string, healthId: string, token?: string | null) {
      return request<UserHealthProfileDto>(`/api/users/${id}/health/${healthId}`, { method: "GET" }, token);
    },
    updateHealthProfile(id: string, healthId: string, payload: UserHealthProfileRequestDto, token?: string | null) {
      return request<UserHealthProfileDto>(`/api/users/${id}/health/${healthId}`, {
        method: "PUT",
        body: JSON.stringify(payload),
      }, token);
    },
    removeHealthProfile(id: string, healthId: string, token?: string | null) {
      return request<UserHealthProfileDto>(`/api/users/${id}/health/${healthId}`, { method: "DELETE" }, token);
    },
    createVerificationRequest(id: string, token?: string | null) {
      return request<EmailVerificationDto>(`/api/users/${id}/verify`, { method: "POST" }, token);
    },
    getVerificationStatus(id: string, token?: string | null) {
      return request<EmailVerificationDto>(`/api/users/${id}/verify`, { method: "GET" }, token);
    },
    verify(id: string, verifyToken: string, token?: string | null) {
      return request<EmailVerificationDto>(`/api/users/${id}/verify/${verifyToken}`, { method: "GET" }, token);
    },
    resetPassword(id: string, payload: ResetPasswordRequestDto, token?: string | null) {
      return request<void>(`/api/users/${id}/reset-password`, {
        method: "POST",
        body: JSON.stringify(payload),
      }, token);
    },
  },
  seo: {
    getContent(token?: string | null) {
      return request<string>("/api/seo/content", { method: "GET" }, token);
    },
  },
};
