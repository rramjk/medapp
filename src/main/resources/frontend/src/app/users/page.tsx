"use client";

import Link from "next/link";
import { useEffect, useMemo, useState } from "react";
import { AppShell } from "@/components/app-shell";
import { ErrorMessage } from "@/components/error-message";
import { LoadingBlock } from "@/components/loading-block";
import { PageHeader } from "@/components/page-header";
import { ProtectedNotice } from "@/components/protected-notice";
import { api } from "@/lib/api";
import type { UserDto } from "@/lib/types";
import { useAuth } from "@/contexts/auth-context";
import { formatDateTime } from "@/lib/utils";

export default function UsersPage() {
  const { token, isAuthenticated, email } = useAuth();
  const [items, setItems] = useState<UserDto[]>([]);
  const [search, setSearch] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!isAuthenticated || !token) return;

    let isMounted = true;

    async function loadUsers() {
      setLoading(true);
      setError(null);
      try {
        const response = await api.users.list(token);
        if (!isMounted) return;
        setItems(response);
      } catch (err) {
        if (!isMounted) return;
        setError(err instanceof Error ? err.message : "Не удалось загрузить пользователей");
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    }

    void loadUsers();
    return () => {
      isMounted = false;
    };
  }, [isAuthenticated, token]);

  const filteredItems = useMemo(() => {
    const needle = search.trim().toLowerCase();
    if (!needle) return items;

    return items.filter((item) => {
      const haystack = `${item.firstName} ${item.lastName} ${item.email} ${item.roleName}`.toLowerCase();
      return haystack.includes(needle);
    });
  }, [items, search]);

  if (!isAuthenticated) {
    return (
      <AppShell>
        <div className="page">
          <PageHeader title="Пользователи" description="Раздел управления пользователями и связанными сущностями." />
          <ProtectedNotice title="Для списка пользователей нужен JWT" />
        </div>
      </AppShell>
    );
  }

  return (
    <AppShell>
      <div className="page">
        <PageHeader
          title="Пользователи"
          description="Просмотр всех пользователей, переход в детальную карточку, редактирование, верификация email, история просмотров и профиль здоровья."
          actions={
            <div className="inline-actions">
              <Link href="/users/new" className="button">
                Создать пользователя
              </Link>
              <span className="badge">Всего: {filteredItems.length}</span>
            </div>
          }
        />

        <section className="card">
          <label className="field">
            <span>Поиск</span>
            <input
              className="input"
              value={search}
              onChange={(event) => setSearch(event.target.value)}
              placeholder="Имя, email, роль"
            />
          </label>
        </section>

        <ErrorMessage message={error} />
        {loading ? <LoadingBlock label="Загружаем список пользователей…" /> : null}

        <section className="list-cards">
          {filteredItems.length === 0 && !loading ? <div className="empty">Пользователи не найдены.</div> : null}

          {filteredItems.map((item) => (
            <article key={item.id} className="card item-card">
              <div className="item-card-header">
                <div>
                  <p className="eyebrow">{item.roleName}</p>
                  <h3>{item.firstName} {item.lastName}</h3>
                  <p className="muted">{item.email}</p>
                </div>
                <div className="inline-actions">
                  {item.email === email ? <span className="badge">Текущий пользователь</span> : null}
                  <Link href={`/users/${item.id}`} className="button secondary">
                    Открыть карточку
                  </Link>
                </div>
              </div>

              <div className="badge-list">
                <span className="badge">Email verified: {item.emailVerified ? "yes" : "no"}</span>
                <span className="badge">Создан: {formatDateTime(item.createdDate)}</span>
                <span className="badge">Изменён: {formatDateTime(item.modifiedDate)}</span>
              </div>
            </article>
          ))}
        </section>
      </div>
    </AppShell>
  );
}
