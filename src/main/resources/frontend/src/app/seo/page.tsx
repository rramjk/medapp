"use client";

import { useEffect, useMemo, useState } from "react";
import { AppShell } from "@/components/app-shell";
import { ErrorMessage } from "@/components/error-message";
import { LoadingBlock } from "@/components/loading-block";
import { PageHeader } from "@/components/page-header";
import { ProtectedNotice } from "@/components/protected-notice";
import { useAuth } from "@/contexts/auth-context";
import { api } from "@/lib/api";

export default function SeoPage() {
  const { token, isAuthenticated } = useAuth();
  const [content, setContent] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!isAuthenticated || !token) return;

    let isMounted = true;

    async function loadSeo() {
      setLoading(true);
      setError(null);
      try {
        const response = await api.seo.getContent(token);
        if (!isMounted) return;
        setContent(response);
      } catch (err) {
        if (!isMounted) return;
        setError(err instanceof Error ? err.message : "Не удалось загрузить SEO-контент");
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    }

    void loadSeo();
    return () => {
      isMounted = false;
    };
  }, [isAuthenticated, token]);

  const keywordCount = useMemo(() => {
    if (!content) return 0;
    return content.split(",").map((item) => item.trim()).filter(Boolean).length;
  }, [content]);

  if (!isAuthenticated) {
    return (
      <AppShell>
        <div className="page">
          <PageHeader title="SEO" description="Сервисная страница для GET /api/seo/content." />
          <ProtectedNotice title="SEO endpoint тоже защищён JWT" />
        </div>
      </AppShell>
    );
  }

  return (
    <AppShell>
      <div className="page">
        <PageHeader
          title="SEO-контент"
          description="Backend агрегирует ключевые слова из статических значений и списка названий препаратов."
          actions={<span className="badge">Ключевых фраз: {keywordCount}</span>}
        />

        <ErrorMessage message={error} />
        {loading ? <LoadingBlock label="Загружаем SEO-контент…" /> : null}

        <section className="card">
          <h3>Содержимое</h3>
          <textarea className="textarea" value={content} readOnly />
        </section>
      </div>
    </AppShell>
  );
}
