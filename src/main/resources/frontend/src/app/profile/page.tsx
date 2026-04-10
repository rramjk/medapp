"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { AppShell } from "@/components/app-shell";
import { ErrorMessage } from "@/components/error-message";
import { LoadingBlock } from "@/components/loading-block";
import { PageHeader } from "@/components/page-header";
import { ProtectedNotice } from "@/components/protected-notice";
import { useAuth } from "@/contexts/auth-context";
import { api } from "@/lib/api";
import type { UserDto } from "@/lib/types";

export default function ProfilePage() {
  const { token, isAuthenticated, email } = useAuth();
  const [currentUser, setCurrentUser] = useState<UserDto | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!isAuthenticated || !token || !email) return;

    let isMounted = true;

    async function resolveCurrentUser() {
      setLoading(true);
      setError(null);
      try {
        const users = await api.users.list(token);
        if (!isMounted) return;
        setCurrentUser(users.find((item) => item.email === email) ?? null);
      } catch (err) {
        if (!isMounted) return;
        setError(err instanceof Error ? err.message : "Не удалось определить текущего пользователя");
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    }

    void resolveCurrentUser();
    return () => {
      isMounted = false;
    };
  }, [email, isAuthenticated, token]);

  if (!isAuthenticated) {
    return (
      <AppShell>
        <div className="page">
          <PageHeader title="Мой профиль" description="Связка JWT-сессии с сущностью пользователя backend-системы." />
          <ProtectedNotice title="Сначала нужен login" />
        </div>
      </AppShell>
    );
  }

  return (
    <AppShell>
      <div className="page">

        <ErrorMessage message={error} />
        {loading ? <LoadingBlock label="Определяем текущего пользователя…" /> : null}

        {currentUser ? (
          <section className="card">
            <h3>{currentUser.firstName} {currentUser.lastName}</h3>
            <p className="muted">{currentUser.email}</p>
            <div className="badge-list">
              <span className="badge">Role: {currentUser.roleName}</span>
              <span className="badge">Email verified: {currentUser.emailVerified ? "yes" : "no"}</span>
            </div>
            <div className="inline-actions">
              <Link href={`/users/${currentUser.id}`} className="button">
                Открыть мою карточку
              </Link>
            </div>
          </section>
        ) : !loading ? (
          <div className="empty">
            Не удалось сопоставить JWT subject с пользователем. Проверь email в токене или добавь backend endpoint /api/users/me.
          </div>
        ) : null}
      </div>
    </AppShell>
  );
}
