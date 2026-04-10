"use client";

import { createContext, useContext, useEffect, useMemo, useState } from "react";
import { useRouter } from "next/navigation";
import { api } from "@/lib/api";
import { getEmailFromToken } from "@/lib/jwt";
import { clearAccessToken, readAccessToken, writeAccessToken } from "@/lib/storage";

interface AuthContextValue {
  token: string | null;
  email: string | null;
  isAuthenticated: boolean;
  isReady: boolean;
  login: (email: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [token, setToken] = useState<string | null>(null);
  const [email, setEmail] = useState<string | null>(null);
  const [isReady, setIsReady] = useState(false);
  const router = useRouter();

  useEffect(() => {
    const storedToken = readAccessToken();
    setToken(storedToken);
    setEmail(getEmailFromToken(storedToken));
    setIsReady(true);
  }, []);

  const value = useMemo<AuthContextValue>(
    () => ({
      token,
      email,
      isAuthenticated: Boolean(token),
      isReady,
      async login(loginValue: string, password: string) {
        const response = await api.auth.login({ email: loginValue, password });
        writeAccessToken(response.accessToken);
        setToken(response.accessToken);
        setEmail(getEmailFromToken(response.accessToken));
      },
      async logout() {
        try {
          if (token) {
            await api.auth.logout(token);
          }
        } finally {
          clearAccessToken();
          setToken(null);
          setEmail(null);
          router.push("/login");
          router.refresh();
        }
      },
    }),
    [isReady, router, token],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthContextValue {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within AuthProvider");
  }
  return context;
}
