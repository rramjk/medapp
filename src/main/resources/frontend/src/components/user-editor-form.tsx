"use client";

import { useEffect, useMemo, useState } from "react";
import type { GenderType, UserDto, UserRequestDto } from "@/lib/types";
import { ErrorMessage } from "@/components/error-message";

interface UserEditorFormProps {
  mode: "create" | "update";
  initialUser?: Partial<UserDto>;
  submitLabel: string;
  note?: string;
  onSubmit: (payload: UserRequestDto) => Promise<void>;
}

interface FormState {
  firstName: string;
  lastName: string;
  birthDate: string;
  gender: GenderType;
  email: string;
  password: string;
  userConsent: boolean;
  privacyConsent: boolean;
}

function mapInitialState(initialUser?: Partial<UserDto>): FormState {
  return {
    firstName: initialUser?.firstName ?? "",
    lastName: initialUser?.lastName ?? "",
    birthDate: initialUser?.birthDate ? initialUser.birthDate.slice(0, 10) : "",
    gender: initialUser?.gender ?? "MALE",
    email: initialUser?.email ?? "",
    password: "",
    userConsent: initialUser?.userConsent ?? false,
    privacyConsent: initialUser?.privacyConsent ?? false,
  };
}

export function UserEditorForm({ mode, initialUser, submitLabel, note, onSubmit }: UserEditorFormProps) {
  const [form, setForm] = useState<FormState>(mapInitialState(initialUser));
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const title = useMemo(
    () => (mode === "create" ? "Создание пользователя" : "Редактирование пользователя"),
    [mode],
  );

  useEffect(() => {
    setForm(mapInitialState(initialUser));
  }, [initialUser]);

  function updateField<K extends keyof FormState>(key: K, value: FormState[K]) {
    setForm((current) => ({ ...current, [key]: value }));
  }

  async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError(null);
    setSuccess(null);
    setLoading(true);

    try {
      await onSubmit({
        firstName: form.firstName.trim(),
        lastName: form.lastName.trim(),
        birthDate: form.birthDate,
        gender: form.gender,
        email: form.email.trim(),
        password: form.password,
        userConsent: form.userConsent,
        privacyConsent: form.privacyConsent,
      });
      setSuccess(mode === "create" ? "Пользователь успешно создан." : "Пользователь успешно обновлён.");
      if (mode === "create") {
        setForm(mapInitialState());
      } else {
        setForm((current) => ({ ...current, password: "" }));
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : "Не удалось выполнить операцию");
    } finally {
      setLoading(false);
    }
  }

  return (
    <form className="card form-card" onSubmit={handleSubmit}>
      <div className="section-heading">
        <div>
          <h3>{title}</h3>
          <p className="muted">
            {note ??
              "Форма соответствует UserRequestDto из backend. Для обновления backend тоже требует пароль, поэтому поле пароля обязательно."}
          </p>
        </div>
      </div>

      <ErrorMessage message={error} />
      {success ? <div className="alert alert-success">{success}</div> : null}

      <div className="form-grid two-columns">
        <label className="field">
          <span>Имя</span>
          <input
            className="input"
            value={form.firstName}
            onChange={(event) => updateField("firstName", event.target.value)}
            required
          />
        </label>

        <label className="field">
          <span>Фамилия</span>
          <input
            className="input"
            value={form.lastName}
            onChange={(event) => updateField("lastName", event.target.value)}
            required
          />
        </label>

        <label className="field">
          <span>Дата рождения</span>
          <input
            className="input"
            type="date"
            value={form.birthDate}
            onChange={(event) => updateField("birthDate", event.target.value)}
            required
          />
        </label>

        <label className="field">
          <span>Пол</span>
          <select
            className="input"
            value={form.gender}
            onChange={(event) => updateField("gender", event.target.value as GenderType)}
          >
            <option value="MALE">MALE</option>
            <option value="FEMALE">FEMALE</option>
          </select>
        </label>

        <label className="field">
          <span>Email</span>
          <input
            className="input"
            type="email"
            value={form.email}
            onChange={(event) => updateField("email", event.target.value)}
            required
          />
        </label>

        <label className="field">
          <span>{mode === "create" ? "Пароль" : "Новый или текущий пароль"}</span>
          <input
            className="input"
            type="password"
            value={form.password}
            onChange={(event) => updateField("password", event.target.value)}
            required
          />
        </label>
      </div>

      <div className="checkbox-row">
        <label className="checkbox-item">
          <input
            type="checkbox"
            checked={form.userConsent}
            onChange={(event) => updateField("userConsent", event.target.checked)}
          />
          <span>Согласие с пользовательским соглашением</span>
        </label>

        <label className="checkbox-item">
          <input
            type="checkbox"
            checked={form.privacyConsent}
            onChange={(event) => updateField("privacyConsent", event.target.checked)}
          />
          <span>Согласие с политикой конфиденциальности</span>
        </label>
      </div>

      <div className="inline-actions">
        <button type="submit" className="button" disabled={loading}>
          {loading ? "Сохранение…" : submitLabel}
        </button>
      </div>
    </form>
  );
}
