import Link from "next/link";

export function ProtectedNotice({ title = "Нужна авторизация" }: { title?: string }) {
  return (
    <div className="card">
      <h3>{title}</h3>
      <p className="muted">
        Этот backend защищает почти весь API через JWT. Сначала войди в систему или создай пользователя.
      </p>
      <div className="inline-actions">
        <Link href="/login" className="button">
          Войти
        </Link>
        <Link href="/register" className="button secondary">
          Зарегистрироваться
        </Link>
      </div>
    </div>
  );
}
