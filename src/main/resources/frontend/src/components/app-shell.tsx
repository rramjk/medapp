"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { useAuth } from "@/contexts/auth-context";
import { cx } from "@/lib/utils";

const privateLinks = [
  { href: "/", label: "Главная страница" },
  { href: "/medicals", label: "Препараты" },
  { href: "/users", label: "Пользователи" },
  { href: "/profile", label: "Мой профиль" },
  { href: "/seo", label: "SEO" },
];

const publicLinks = [
  { href: "/login", label: "Вход" },
  { href: "/register", label: "Регистрация" },
  { href: "/verify", label: "Верификация email" },
];

export function AppShell({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();
  const { email, isAuthenticated, logout, isReady } = useAuth();
  const links = isAuthenticated ? privateLinks : publicLinks;

  return (
    <div className="shell">
      <aside className="sidebar">
        <div>
          <div className="brand-card">
            <div className="brand-pill">MA</div>
            <div>
              <p className="eyebrow">Medical Applied</p>
              <h1 className="brand-title">Control Panel</h1>
            </div>
          </div>

          <nav className="nav-list">
            {links.map((link) => (
              <Link
                key={link.href}
                href={link.href}
                className={cx("nav-link", pathname === link.href && "active")}
              >
                {link.label}
              </Link>
            ))}
          </nav>
        </div>

        <div className="sidebar-footer">
          <div className="session-card">
            <p className="eyebrow">Сессия</p>
            {!isReady ? (
              <p className="muted">Загрузка…</p>
            ) : isAuthenticated ? (
              <>
                <p className="session-email">{email}</p>
                <button type="button" className="button secondary full-width" onClick={() => void logout()}>
                  Выйти
                </button>
              </>
            ) : (
              <p className="muted">Гостевой режим. Доступны только вход, регистрация и подготовка к верификации.</p>
            )}
          </div>
        </div>
      </aside>

      <main className="content">{children}</main>
    </div>
  );
}
