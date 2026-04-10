const ACCESS_TOKEN_KEY = "medical-applied.access-token";
const HEALTH_PROFILE_MAP_KEY = "medical-applied.health-profile-map";

export function readAccessToken(): string | null {
  if (typeof window === "undefined") return null;
  return window.localStorage.getItem(ACCESS_TOKEN_KEY);
}

export function writeAccessToken(token: string): void {
  if (typeof window === "undefined") return;
  window.localStorage.setItem(ACCESS_TOKEN_KEY, token);
}

export function clearAccessToken(): void {
  if (typeof window === "undefined") return;
  window.localStorage.removeItem(ACCESS_TOKEN_KEY);
}

function readHealthMap(): Record<string, string> {
  if (typeof window === "undefined") return {};

  const raw = window.localStorage.getItem(HEALTH_PROFILE_MAP_KEY);
  if (!raw) return {};

  try {
    return JSON.parse(raw) as Record<string, string>;
  } catch {
    return {};
  }
}

function writeHealthMap(value: Record<string, string>): void {
  if (typeof window === "undefined") return;
  window.localStorage.setItem(HEALTH_PROFILE_MAP_KEY, JSON.stringify(value));
}

export function readHealthProfileId(userId: string): string {
  return readHealthMap()[userId] ?? "";
}

export function writeHealthProfileId(userId: string, healthId: string): void {
  const current = readHealthMap();
  current[userId] = healthId;
  writeHealthMap(current);
}

export function clearHealthProfileId(userId: string): void {
  const current = readHealthMap();
  delete current[userId];
  writeHealthMap(current);
}
