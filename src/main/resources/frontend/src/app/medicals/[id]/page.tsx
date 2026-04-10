"use client";

import Link from "next/link";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";
import { AppShell } from "@/components/app-shell";
import { ErrorMessage } from "@/components/error-message";
import { LoadingBlock } from "@/components/loading-block";
import { PageHeader } from "@/components/page-header";
import { ProtectedNotice } from "@/components/protected-notice";
import { api } from "@/lib/api";
import type { MedicalDto } from "@/lib/types";
import { useAuth } from "@/contexts/auth-context";
import { boolLabel } from "@/lib/utils";

export default function MedicalDetailsPage() {
  const params = useParams<{ id: string }>();
  const id = params.id;
  const { token, isAuthenticated } = useAuth();
  const [item, setItem] = useState<MedicalDto | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!isAuthenticated || !token || !id) return;

    let isMounted = true;

    async function loadMedical() {
      setLoading(true);
      setError(null);
      try {
        const response = await api.medicals.getById(id, token);
        if (!isMounted) return;
        setItem(response);
      } catch (err) {
        if (!isMounted) return;
        setError(err instanceof Error ? err.message : "Не удалось загрузить карточку препарата");
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    }

    void loadMedical();
    return () => {
      isMounted = false;
    };
  }, [id, isAuthenticated, token]);

  if (!isAuthenticated) {
    return (
      <AppShell>
        <div className="page">
          <PageHeader title="Карточка препарата" description="Детальная карточка работает через защищённый GET /api/medicals/{id}." />
          <ProtectedNotice />
        </div>
      </AppShell>
    );
  }

  return (
    <AppShell>
      <div className="page">
        <PageHeader
          title="Карточка препарата"
          description="Детальное представление MedicalDto. Открытие этой страницы должно попадать в историю просмотров пользователя на backend."
          actions={
            <Link href="/medicals" className="button secondary">
              Назад к каталогу
            </Link>
          }
        />

        <ErrorMessage message={error} />
        {loading ? <LoadingBlock label="Загружаем карточку препарата…" /> : null}

        {item ? (
          <>
            <section className="card">
              <p className="eyebrow">{item.countryRu} / {item.countryEn}</p>
              <h3>{item.name}</h3>
              <p className="muted">{item.type} · {item.activeIngredient}</p>
            </section>

            <section className="two-panel">
              <article className="card">
                <h3>Описание</h3>
                <div className="kv">
                  <div className="kv-row">
                    <dt>ID</dt>
                    <dd>{item.id}</dd>
                  </div>
                  <div className="kv-row">
                    <dt>Описание</dt>
                    <dd>{item.description}</dd>
                  </div>
                  <div className="kv-row">
                    <dt>Показания</dt>
                    <dd>{item.indications}</dd>
                  </div>
                  <div className="kv-row">
                    <dt>Противопоказания</dt>
                    <dd>{item.contraindications}</dd>
                  </div>
                  <div className="kv-row">
                    <dt>Дозировка</dt>
                    <dd>{item.dosing}</dd>
                  </div>
                </div>
              </article>

              <article className="card">
                <h3>Friendly-флаги</h3>
                <div className="kv">
                  <div className="kv-row"><dt>Для почек</dt><dd>{boolLabel(item.kidneyFriendly)}</dd></div>
                  <div className="kv-row"><dt>При беременности</dt><dd>{boolLabel(item.pregnantFriendly)}</dd></div>
                  <div className="kv-row"><dt>При грудном вскармливании</dt><dd>{boolLabel(item.breastfedFriendly)}</dd></div>
                  <div className="kv-row"><dt>Для печени</dt><dd>{boolLabel(item.liverFriendly)}</dd></div>
                  <div className="kv-row"><dt>Для детей</dt><dd>{boolLabel(item.childFriendly)}</dd></div>
                  <div className="kv-row"><dt>Для желудка</dt><dd>{boolLabel(item.stomachFriendly)}</dd></div>
                </div>
              </article>
            </section>
          </>
        ) : null}
      </div>
    </AppShell>
  );
}
