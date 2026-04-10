"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { AppShell } from "@/components/app-shell";
import { FeatureCard } from "@/components/feature-card";
import { LoadingBlock } from "@/components/loading-block";
import { PageHeader } from "@/components/page-header";
import { StatCard } from "@/components/stat-card";
import { api, getApiBaseUrl } from "@/lib/api";
import { useAuth } from "@/contexts/auth-context";

interface DashboardStats {
  users: number;
  names: number;
  categories: number;
  countries: number;
}

export default function HomePage() {
  const { token, isAuthenticated, isReady } = useAuth();
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!isAuthenticated || !token) {
      setStats(null);
      return;
    }

    let isMounted = true;

    async function loadDashboard() {
      setLoading(true);
      setError(null);
      try {
        const [users, names, categories, countries] = await Promise.all([
          api.users.list(token),
          api.medicals.getNames(token),
          api.medicals.getCategories(token),
          api.medicals.getCountries(false, token),
        ]);

        if (!isMounted) return;
        setStats({
          users: users.length,
          names: names.length,
          categories: categories.length,
          countries: countries.length,
        });
      } catch (err) {
        if (!isMounted) return;
        setError(err instanceof Error ? err.message : "Не удалось загрузить основные данные страницы");
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    }

    void loadDashboard();

    return () => {
      isMounted = false;
    };
  }, [isAuthenticated, token]);

  return (
    <AppShell>
      <div className="page">
        <PageHeader
          title="Medical Applied"
          description="Информационная платформа для поиска лекарственных препаратов, сравнения доступных вариантов и безопасной навигации по медицинской справочной информации."
          actions={
            <div className="inline-actions">
              <Link href="/medicals" className="button">
                Найти препараты
              </Link>
              <Link href="/profile" className="button secondary">
                Открыть профиль
              </Link>
            </div>
          }
        />

        <section className="card info-card info-card-accent">
          <p className="eyebrow">Важная информация</p>
          <h3>Medical Applied — информационный сервис, а не замена врачу</h3>
          <div className="info-text">
            <p>
              Платформа помогает искать и сопоставлять лекарственные препараты, просматривать сведения о странах,
              категориях и доступных наименованиях, а также работать с персональным профилем пользователя.
              При этом сервис не осуществляет медицинскую деятельность, не назначает лечение и не выдаёт
              персональные медицинские рекомендации.
            </p>
            <p>
              Все сведения на платформе предназначены для ознакомления. Подбор препарата, оценка показаний,
              противопоказаний, совместимости, дозировки и схемы применения должны выполняться вместе с лечащим
              врачом и с обязательной проверкой официальной инструкции к лекарственному средству.
            </p>
            <p>
              Найденные аналоги и карточки препаратов не гарантируют полное клиническое соответствие в разных странах.
              Пользователь самостоятельно принимает решение о выборе препарата и несёт ответственность за его
              применение.
            </p>
          </div>
        </section>

        {!isReady ? <LoadingBlock label="Подготавливаем пользовательскую сессию…" /> : null}

        <section className="card-grid">
          <FeatureCard
            badge="catalog"
            title="Поиск препаратов"
            description="Открывай каталог, фильтруй данные по справочникам и переходи в карточки препаратов с детализированной информацией."
          />
          <FeatureCard
            badge="account"
            title="Работа с аккаунтом"
            description="Создавай, редактируй и удаляй пользователей, а также управляй связанными сущностями на персональной странице."
          />
          <FeatureCard
            badge="health"
            title="Профиль здоровья"
            description="Храни сведения о состоянии здоровья в отдельной сущности и используй их как часть пользовательского сценария приложения."
          />
          <FeatureCard
            badge="security"
            title="Email и безопасность"
            description="Подтверждай email, отслеживай статус верификации и работай со сценариями смены пароля и входа в систему."
          />
          <FeatureCard
            badge="api"
            title="REST-интеграция"
            description="Интерфейс опирается на твой Spring Boot backend и транслирует его функции в понятный пользовательский UI."
          />
          <FeatureCard
            badge="roadmap"
            title="Дальнейшее развитие"
            description="Архитектура страницы уже готова для расширения: можно добавить рекомендации, врачебные сценарии и отдельные аналитические блоки."
          />
        </section>
         <section className="two-panel">
          <article className="card info-card">
            <p className="eyebrow">Как использовать платформу</p>
            <h3>Основной пользовательский сценарий</h3>
            <div className="info-text">
              <p>
                1. Зарегистрируй пользователя и выполни вход, чтобы получить доступ к защищённым REST-операциям.
              </p>
              <p>
                2. Перейди в каталог препаратов, выбери страну, категорию или название и открой детальную карточку.
              </p>
              <p>
                3. Используй профиль пользователя для хранения данных аккаунта, истории и связанной информации о здоровье.
              </p>
              <p>
                4. При необходимости выполни верификацию email и управляй параметрами безопасности через экран смены пароля.
              </p>
            </div>
          </article>
        </section>
      </div>
    </AppShell>
  );
}
