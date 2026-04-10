"use client";

import Link from "next/link";
import { useRouter, useSearchParams } from "next/navigation";
import { useState } from "react";
import { AppShell } from "@/components/app-shell";
import { ErrorMessage } from "@/components/error-message";
import { PageHeader } from "@/components/page-header";
import { useAuth } from "@/contexts/auth-context";

export default function LoginPage() {
  const { login, isAuthenticated, email } = useAuth();
  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const router = useRouter();
  const searchParams = useSearchParams();

  async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError(null);
    setLoading(true);

    try {
      await login(form.email, form.password);
      const redirectTo = searchParams.get("redirectTo");
      router.push(redirectTo || "/");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Не удалось выполнить вход");
    } finally {
      setLoading(false);
    }
  }

  return (
    <AppShell>
      <div className="page">
        <PageHeader
          title="Вход"
          description="JWT-аутентификация для доступа к защищённым backend endpoint-ам."
        />

        <form className="card form-card" onSubmit={handleSubmit}>
          <h3>Авторизация</h3>
          <p className="muted">
            Backend возвращает только accessToken, поэтому frontend хранит токен локально и извлекает email из JWT payload.
          </p>

          {isAuthenticated ? (
            <div className="alert alert-success">Ты уже вошёл как {email}.</div>
          ) : null}
          <ErrorMessage message={error} />

          <div className="form-grid">
            <label className="field">
              <span>Email</span>
              <input
                className="input"
                type="email"
                value={form.email}
                onChange={(event) => setForm((current) => ({ ...current, email: event.target.value }))}
                required
              />
            </label>

            <label className="field">
              <span>Пароль</span>
              <input
                className="input"
                type="password"
                value={form.password}
                onChange={(event) => setForm((current) => ({ ...current, password: event.target.value }))}
                required
              />
            </label>
          </div>

          <div className="inline-actions">
            <button type="submit" className="button" disabled={loading}>
              {loading ? "Входим…" : "Войти"}
            </button>
            <Link href="/register" className="button secondary">
              Создать пользователя
            </Link>
          </div>
        </form>
      </div>
    </AppShell>
  );
}
