import type { JwtPayload } from "@/lib/types";

export function parseJwt(token?: string | null): JwtPayload | null {
  if (!token) return null;

  try {
    const parts = token.split(".");
    if (parts.length < 2) return null;

    const base64 = parts[1].replace(/-/g, "+").replace(/_/g, "/");
    const padded = base64 + "=".repeat((4 - (base64.length % 4 || 4)) % 4);
    const json = atob(padded);
    return JSON.parse(json) as JwtPayload;
  } catch {
    return null;
  }
}

export function getEmailFromToken(token?: string | null): string | null {
  return parseJwt(token)?.sub?.toString() ?? null;
}
