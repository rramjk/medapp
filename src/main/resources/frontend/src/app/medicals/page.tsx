"use client";

import Link from "next/link";
import { useEffect, useMemo, useState } from "react";
import { AppShell } from "@/components/app-shell";
import { ErrorMessage } from "@/components/error-message";
import { LoadingBlock } from "@/components/loading-block";
import { PageHeader } from "@/components/page-header";
import { ProtectedNotice } from "@/components/protected-notice";
import { api } from "@/lib/api";
import type { MedicalDto } from "@/lib/types";
import { useAuth } from "@/contexts/auth-context";
import { boolLabel } from "@/lib/utils";

export default function MedicalsPage() {
  const { token, isAuthenticated } = useAuth();
  const [items, setItems] = useState<MedicalDto[]>([]);
  const [countries, setCountries] = useState<string[]>([]);
  const [categories, setCategories] = useState<string[]>([]);
  const [names, setNames] = useState<string[]>([]);
  const [filters, setFilters] = useState({ countryEn: "", category: "", name: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!isAuthenticated || !token) return;

    let isMounted = true;

    async function loadMeta() {
      try {
        const [loadedCountries, loadedCategories, loadedNames] = await Promise.all([
          api.medicals.getCountries(false, token),
          api.medicals.getCategories(token),
          api.medicals.getNames(token),
        ]);
        if (!isMounted) return;
        setCountries(loadedCountries);
        setCategories(loadedCategories);
        setNames(loadedNames);
      } catch (err) {
        if (!isMounted) return;
        setError(err instanceof Error ? err.message : "Не удалось загрузить справочники");
      }
    }

    void loadMeta();

    return () => {
      isMounted = false;
    };
  }, [isAuthenticated, token]);

  useEffect(() => {
    if (!isAuthenticated || !token) return;
    void search();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isAuthenticated, token]);

  async function search() {
    if (!token) return;
    setLoading(true);
    setError(null);
    try {
      const response = await api.medicals.list(filters, token);
      setItems(response);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Не удалось загрузить препараты");
    } finally {
      setLoading(false);
    }
  }

  const totalLabel = useMemo(() => `${items.length} записей`, [items.length]);

  if (!isAuthenticated) {
    return (
      <AppShell>
        <div className="page">
          <PageHeader title="Каталог препаратов" description="Этот раздел использует защищённые medical endpoints backend-приложения." />
          <ProtectedNotice title="Для каталога препаратов нужен JWT" />
        </div>
      </AppShell>
    );
  }

  return (
    <AppShell>
      <div className="page">
        <PageHeader
          title="Каталог препаратов"
          description="Фильтрация по стране, категории и названию. Открытие детали препарата автоматически записывает его в историю просмотров пользователя на backend."
          actions={<span className="badge">{totalLabel}</span>}
        />

        <section className="card form-card">
          <div className="section-heading">
            <div>
              <h3>Фильтры поиска</h3>
              <p className="muted">Используются query-параметры countryEn, category и name.</p>
            </div>
          </div>

          <ErrorMessage message={error} />

          <div className="form-grid two-columns">
            <label className="field">
              <span>Страна</span>
              <input
                className="input"
                list="country-options"
                value={filters.countryEn}
                onChange={(event) => setFilters((current) => ({ ...current, countryEn: event.target.value }))}
                placeholder="Germany"
              />
              <datalist id="country-options">
                {countries.map((item) => (
                  <option key={item} value={item} />
                ))}
              </datalist>
            </label>

            <label className="field">
              <span>Категория</span>
              <input
                className="input"
                list="category-options"
                value={filters.category}
                onChange={(event) => setFilters((current) => ({ ...current, category: event.target.value }))}
                placeholder="Обезболивающее"
              />
              <datalist id="category-options">
                {categories.map((item) => (
                  <option key={item} value={item} />
                ))}
              </datalist>
            </label>

            <label className="field">
              <span>Название</span>
              <input
                className="input"
                list="name-options"
                value={filters.name}
                onChange={(event) => setFilters((current) => ({ ...current, name: event.target.value }))}
                placeholder="Нурофен"
              />
              <datalist id="name-options">
                {names.map((item) => (
                  <option key={item} value={item} />
                ))}
              </datalist>
            </label>
          </div>

          <div className="inline-actions">
            <button type="button" className="button" onClick={() => void search()} disabled={loading}>
              {loading ? "Поиск…" : "Найти препараты"}
            </button>
            <button
              type="button"
              className="button secondary"
              onClick={() => {
                setFilters({ countryEn: "", category: "", name: "" });
                setItems([]);
              }}
            >
              Сбросить
            </button>
          </div>
        </section>

        {loading ? <LoadingBlock label="Загружаем препараты…" /> : null}

        <section className="list-cards">
          {items.length === 0 && !loading ? (
            <div className="empty">По текущим фильтрам ничего не найдено.</div>
          ) : null}

          {items.map((item) => (
            <article key={item.id} className="card item-card">
              <div className="item-card-header">
                <div>
                  <p className="eyebrow">{item.countryRu} / {item.countryEn}</p>
                  <h3>{item.name}</h3>
                  <p className="muted">{item.type} · {item.activeIngredient}</p>
                </div>
                <Link href={`/medicals/${item.id}`} className="button secondary">
                  Открыть карточку
                </Link>
              </div>

              <p className="muted">{item.description}</p>

              <div className="flag-list">
                <span className={`flag ${item.childFriendly ? "good" : "bad"}`}>Для детей: {boolLabel(item.childFriendly)}</span>
                <span className={`flag ${item.kidneyFriendly ? "good" : "bad"}`}>Почки: {boolLabel(item.kidneyFriendly)}</span>
                <span className={`flag ${item.liverFriendly ? "good" : "bad"}`}>Печень: {boolLabel(item.liverFriendly)}</span>
                <span className={`flag ${item.stomachFriendly ? "good" : "bad"}`}>Желудок: {boolLabel(item.stomachFriendly)}</span>
              </div>
            </article>
          ))}
        </section>
      </div>
    </AppShell>
  );
}
