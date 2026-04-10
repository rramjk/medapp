"use client";

import Link from "next/link";
import { useSearchParams } from "next/navigation";
import { useEffect, useMemo, useState } from "react";
import { AppShell } from "@/components/app-shell";
import { ErrorMessage } from "@/components/error-message";
import { LoadingBlock } from "@/components/loading-block";
import { PageHeader } from "@/components/page-header";
import { useAuth } from "@/contexts/auth-context";
import { api } from "@/lib/api";
import type { EmailVerificationDto } from "@/lib/types";

export default function VerifyPage() {
  const searchParams = useSearchParams();
  const userId = searchParams.get("userId") ?? "";
  const tokenValue = searchParams.get("token") ?? "";
  const { token, isAuthenticated } = useAuth();
  const [result, setResult] = useState<EmailVerificationDto | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const redirectTarget = useMemo(() => {
    const search = new URLSearchParams();
    if (userId) search.set("userId", userId);
    if (tokenValue) search.set("token", tokenValue);
    return `/verify${search.toString() ? `?${search.toString()}` : ""}`;
  }, [tokenValue, userId]);

  useEffect(() => {
    if (!isAuthenticated || !token || !userId || !tokenValue) return;

    let isMounted = true;

    async function verifyEmail() {
      setLoading(true);
      setError(null);
      try {
        const response = await api.users.verify(userId, tokenValue, token);
        if (!isMounted) return;
        setResult(response);
      } catch (err) {
        if (!isMounted) return;
        setError(err instanceof Error ? err.message : "Не удалось подтвердить email");
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    }

    void verifyEmail();
    return () => {
      isMounted = false;
    };
  }, [isAuthenticated, token, tokenValue, userId]);

  return (
    <AppShell>
      <div className="page">
        <PageHeader
          title="Верификация email"
          description="Помощник для сценария подтверждения email через backend endpoint GET /api/users/{id}/verify/{token}."
        />

        <section className="card form-card">
          <h3>Параметры</h3>
          <div className="kv">
            <div className="kv-row"><dt>userId</dt><dd>{userId || "—"}</dd></div>
            <div className="kv-row"><dt>token</dt><dd>{tokenValue || "—"}</dd></div>
          </div>

          {!userId || !tokenValue ? (
            <div className="empty">
              Передай параметры в query string, например: <br />
              <code>/verify?userId=&lt;UUID&gt;&amp;token=&lt;UUID&gt;</code>
            </div>
          ) : null}

          {!isAuthenticated && userId && tokenValue ? (
            <div className="alert alert-error">
              Backend защищает verify endpoint JWT-аутентификацией. Сначала войди в систему, потом вернись на эту страницу.
            </div>
          ) : null}

          {!isAuthenticated && userId && tokenValue ? (
            <div className="inline-actions">
              <Link href={`/login?redirectTo=${encodeURIComponent(redirectTarget)}`} className="button">
                Войти и подтвердить email
              </Link>
            </div>
          ) : null}

          <ErrorMessage message={error} />
          {loading ? <LoadingBlock label="Подтверждаем email…" /> : null}
          {result ? <div className="alert alert-success">Статус подтверждения: {result.status}</div> : null}
        </section>
      </div>
    </AppShell>
  );
}
