"use client";

import Link from "next/link";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useMemo, useState } from "react";
import { AppShell } from "@/components/app-shell";
import { ErrorMessage } from "@/components/error-message";
import { LoadingBlock } from "@/components/loading-block";
import { PageHeader } from "@/components/page-header";
import { ProtectedNotice } from "@/components/protected-notice";
import { UserEditorForm } from "@/components/user-editor-form";
import { useAuth } from "@/contexts/auth-context";
import { api } from "@/lib/api";
import { clearHealthProfileId, readHealthProfileId, writeHealthProfileId } from "@/lib/storage";
import type {
  EmailVerificationDto,
  MedicalDto,
  UserDto,
  UserHealthProfileDto,
  UserHealthProfileRequestDto,
  UserRequestDto,
} from "@/lib/types";
import { formatDateTime, toMultiline, normalizeMultiline } from "@/lib/utils";

interface HealthFormState {
  weight: string;
  chronicConditions: string;
  healthFeatures: string;
  allergies: string;
}

function mapHealthState(profile?: UserHealthProfileDto | null): HealthFormState {
  return {
    weight: profile?.weight?.toString() ?? "",
    chronicConditions: toMultiline(profile?.chronicConditions),
    healthFeatures: toMultiline(profile?.healthFeatures),
    allergies: toMultiline(profile?.allergies),
  };
}

function toHealthPayload(state: HealthFormState): UserHealthProfileRequestDto {
  return {
    weight: state.weight ? Number(state.weight) : undefined,
    chronicConditions: normalizeMultiline(state.chronicConditions),
    healthFeatures: normalizeMultiline(state.healthFeatures),
    allergies: normalizeMultiline(state.allergies),
  };
}

export default function UserDetailsPage() {
  const params = useParams<{ id: string }>();
  const userId = params.id;
  const router = useRouter();
  const { token, isAuthenticated, email } = useAuth();
  const [user, setUser] = useState<UserDto | null>(null);
  const [views, setViews] = useState<MedicalDto[]>([]);
  const [healthProfile, setHealthProfile] = useState<UserHealthProfileDto | null>(null);
  const [healthId, setHealthId] = useState("");
  const [healthForm, setHealthForm] = useState<HealthFormState>(mapHealthState());
  const [verification, setVerification] = useState<EmailVerificationDto | null>(null);
  const [verificationToken, setVerificationToken] = useState("");
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const isCurrentUser = useMemo(() => user?.email === email, [email, user?.email]);

  useEffect(() => {
    if (!userId) return;
    setHealthId(readHealthProfileId(userId));
  }, [userId]);

  useEffect(() => {
    if (!isAuthenticated || !token || !userId) return;

    let isMounted = true;

    async function bootstrap() {
      setLoading(true);
      setError(null);
      try {
        const [loadedUser, loadedViews] = await Promise.all([
          api.users.getById(userId, token),
          api.users.getMedicalViews(userId, token),
        ]);
        if (!isMounted) return;
        setUser(loadedUser);
        setViews(loadedViews);
      } catch (err) {
        if (!isMounted) return;
        setError(err instanceof Error ? err.message : "Не удалось загрузить пользователя");
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    }

    void bootstrap();
    return () => {
      isMounted = false;
    };
  }, [isAuthenticated, token, userId]);

  function updateHealthField(key: keyof HealthFormState, value: string) {
    setHealthForm((current) => ({ ...current, [key]: value }));
  }

  async function refreshViews() {
    if (!token) return;
    const response = await api.users.getMedicalViews(userId, token);
    setViews(response);
  }

  async function handleUpdateUser(payload: UserRequestDto) {
    if (!token) return;
    setMessage(null);
    setError(null);
    const response = await api.users.update(userId, payload, token);
    setUser(response);
    setMessage("Пользователь обновлён.");
  }

  async function handleDeleteUser() {
    if (!token) return;
    const accepted = window.confirm("Удалить пользователя?");
    if (!accepted) return;

    setMessage(null);
    setError(null);
    try {
      await api.users.remove(userId, token);
      clearHealthProfileId(userId);
      router.push("/users");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Не удалось удалить пользователя");
    }
  }

  async function handleCreateHealth() {
    if (!token) return;
    setMessage(null);
    setError(null);
    try {
      const response = await api.users.createHealthProfile(userId, toHealthPayload(healthForm), token);
      setHealthProfile(response);
      setHealthForm(mapHealthState(response));
      setHealthId(response.id);
      writeHealthProfileId(userId, response.id);
      setMessage(`Профиль здоровья создан. healthId = ${response.id}`);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Не удалось создать профиль здоровья");
    }
  }

  async function handleLoadHealth() {
    if (!token || !healthId) return;
    setMessage(null);
    setError(null);
    try {
      const response = await api.users.getHealthProfile(userId, healthId, token);
      setHealthProfile(response);
      setHealthForm(mapHealthState(response));
      writeHealthProfileId(userId, response.id);
      setMessage("Профиль здоровья загружен.");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Не удалось получить профиль здоровья");
    }
  }

  async function handleUpdateHealth() {
    if (!token || !healthId) return;
    setMessage(null);
    setError(null);
    try {
      const response = await api.users.updateHealthProfile(userId, healthId, toHealthPayload(healthForm), token);
      setHealthProfile(response);
      setHealthForm(mapHealthState(response));
      writeHealthProfileId(userId, response.id);
      setMessage("Профиль здоровья обновлён.");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Не удалось обновить профиль здоровья");
    }
  }

  async function handleDeleteHealth() {
    if (!token || !healthId) return;
    const accepted = window.confirm("Удалить профиль здоровья?");
    if (!accepted) return;

    setMessage(null);
    setError(null);
    try {
      await api.users.removeHealthProfile(userId, healthId, token);
      clearHealthProfileId(userId);
      setHealthId("");
      setHealthProfile(null);
      setHealthForm(mapHealthState());
      setMessage("Профиль здоровья удалён.");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Не удалось удалить профиль здоровья");
    }
  }

  async function handleCreateVerification() {
    if (!token) return;
    setMessage(null);
    setError(null);
    try {
      const response = await api.users.createVerificationRequest(userId, token);
      setVerification(response);
      setMessage(`Запрос на верификацию создан. Статус: ${response.status}`);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Не удалось создать запрос на верификацию");
    }
  }

  async function handleLoadVerification() {
    if (!token) return;
    setMessage(null);
    setError(null);
    try {
      const response = await api.users.getVerificationStatus(userId, token);
      setVerification(response);
      setMessage(`Текущий статус: ${response.status}`);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Не удалось получить статус верификации");
    }
  }

  async function handleVerify() {
    if (!token || !verificationToken) return;
    setMessage(null);
    setError(null);
    try {
      const response = await api.users.verify(userId, verificationToken, token);
      setVerification(response);
      setMessage(`Email подтверждён. Статус: ${response.status}`);
      const refreshedUser = await api.users.getById(userId, token);
      setUser(refreshedUser);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Не удалось подтвердить email");
    }
  }

  async function handleResetPassword() {
    if (!token) return;
    setMessage(null);
    setError(null);
    try {
      await api.users.resetPassword(userId, { oldPassword, newPassword }, token);
      setOldPassword("");
      setNewPassword("");
      setMessage("Пароль обновлён.");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Не удалось сменить пароль");
    }
  }

  async function handleClearViews() {
    if (!token) return;
    const accepted = window.confirm("Очистить историю просмотров препаратов?");
    if (!accepted) return;

    setMessage(null);
    setError(null);
    try {
      await api.users.clearMedicalViews(userId, token);
      await refreshViews();
      setMessage("История просмотров очищена.");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Не удалось очистить историю просмотров");
    }
  }

  if (!isAuthenticated) {
    return (
      <AppShell>
        <div className="page">
          <PageHeader title="Карточка пользователя" description="Управление пользователем и связанными сущностями." />
          <ProtectedNotice title="Для карточки пользователя нужен JWT" />
        </div>
      </AppShell>
    );
  }

  return (
    <AppShell>
      <div className="page">
        <PageHeader
          title="Карточка пользователя"
          description="Один экран покрывает весь доступный backend-функционал, связанный с UserDto: обновление, удаление, health profile, views history, verification и reset password."
          actions={
            <div className="inline-actions">
              <Link href="/users" className="button secondary">
                Назад к списку
              </Link>
              <button type="button" className="button danger" onClick={() => void handleDeleteUser()}>
                Удалить пользователя
              </button>
            </div>
          }
        />

        <ErrorMessage message={error} />
        {message ? <div className="alert alert-success">{message}</div> : null}
        {loading ? <LoadingBlock label="Загружаем пользователя…" /> : null}

        {user ? (
          <>
            <section className="card">
              <p className="eyebrow">{user.roleName}</p>
              <h3>{user.firstName} {user.lastName}</h3>
              <p className="muted">{user.email}</p>
              <div className="badge-list">
                {isCurrentUser ? <span className="badge">Текущая сессия</span> : null}
                <span className="badge">Email verified: {user.emailVerified ? "yes" : "no"}</span>
                <span className="badge">Создан: {formatDateTime(user.createdDate)}</span>
                <span className="badge">Изменён: {formatDateTime(user.modifiedDate)}</span>
              </div>
            </section>

            <UserEditorForm
              mode="update"
              initialUser={user}
              submitLabel="Сохранить пользователя"
              onSubmit={handleUpdateUser}
            />

            <section className="two-panel">
              <article className="card form-card">
                <div className="section-heading">
                  <div>
                    <h3>Профиль здоровья</h3>
                    <p className="muted">
                      В backend нет списка health profiles, поэтому frontend хранит последний созданный healthId локально.
                    </p>
                  </div>
                </div>

                <div className="form-grid">
                  <label className="field">
                    <span>healthId</span>
                    <input className="input" value={healthId} onChange={(event) => setHealthId(event.target.value)} />
                  </label>

                  <label className="field">
                    <span>Вес</span>
                    <input
                      className="input"
                      type="number"
                      step="0.1"
                      value={healthForm.weight}
                      onChange={(event) => updateHealthField("weight", event.target.value)}
                    />
                  </label>

                  <label className="field">
                    <span>Хронические заболевания</span>
                    <textarea
                      className="textarea"
                      value={healthForm.chronicConditions}
                      onChange={(event) => updateHealthField("chronicConditions", event.target.value)}
                      placeholder="По одному пункту в строке"
                    />
                  </label>

                  <label className="field">
                    <span>Особенности здоровья</span>
                    <textarea
                      className="textarea"
                      value={healthForm.healthFeatures}
                      onChange={(event) => updateHealthField("healthFeatures", event.target.value)}
                      placeholder="По одному пункту в строке"
                    />
                  </label>

                  <label className="field">
                    <span>Аллергии</span>
                    <textarea
                      className="textarea"
                      value={healthForm.allergies}
                      onChange={(event) => updateHealthField("allergies", event.target.value)}
                      placeholder="По одному пункту в строке"
                    />
                  </label>
                </div>

                <div className="inline-actions">
                  <button type="button" className="button" onClick={() => void handleCreateHealth()}>
                    Создать
                  </button>
                  <button type="button" className="button secondary" onClick={() => void handleLoadHealth()}>
                    Получить
                  </button>
                  <button type="button" className="button secondary" onClick={() => void handleUpdateHealth()}>
                    Обновить
                  </button>
                  <button type="button" className="button danger" onClick={() => void handleDeleteHealth()}>
                    Удалить
                  </button>
                </div>

                {healthProfile ? (
                  <div className="token-box">
                    Загружен профиль: {healthProfile.id}
                  </div>
                ) : null}
              </article>

              <article className="card form-card">
                <div className="section-heading">
                  <div>
                    <h3>Email verification</h3>
                    <p className="muted">
                      Токен backend не возвращает, он приходит по email. Для подтверждения вставь его вручную или используй страницу /verify.
                    </p>
                  </div>
                </div>

                <div className="form-grid">
                  <label className="field">
                    <span>Токен верификации</span>
                    <input
                      className="input"
                      value={verificationToken}
                      onChange={(event) => setVerificationToken(event.target.value)}
                      placeholder="UUID token"
                    />
                  </label>
                </div>

                <div className="inline-actions">
                  <button type="button" className="button" onClick={() => void handleCreateVerification()}>
                    Создать запрос
                  </button>
                  <button type="button" className="button secondary" onClick={() => void handleLoadVerification()}>
                    Проверить статус
                  </button>
                  <button type="button" className="button secondary" onClick={() => void handleVerify()}>
                    Подтвердить email
                  </button>
                </div>

                {verification ? <div className="token-box">Статус: {verification.status}</div> : null}
              </article>
            </section>

            <section className="two-panel">
              <article className="card form-card">
                <div className="section-heading">
                  <div>
                    <h3>Смена пароля</h3>
                    <p className="muted">Запрос отправляется в ResetPasswordRequestDto.</p>
                  </div>
                </div>

                <div className="form-grid">
                  <label className="field">
                    <span>Старый пароль</span>
                    <input className="input" type="password" value={oldPassword} onChange={(event) => setOldPassword(event.target.value)} />
                  </label>
                  <label className="field">
                    <span>Новый пароль</span>
                    <input className="input" type="password" value={newPassword} onChange={(event) => setNewPassword(event.target.value)} />
                  </label>
                </div>

                <div className="inline-actions">
                  <button type="button" className="button" onClick={() => void handleResetPassword()}>
                    Сменить пароль
                  </button>
                </div>
              </article>

              <article className="card form-card">
                <div className="section-heading">
                  <div>
                    <h3>История просмотров препаратов</h3>
                    <p className="muted">
                      История формируется на backend при открытии GET /api/medicals/{'{id}'} и очищается через DELETE /api/users/{'{id}'}/views.
                    </p>
                  </div>
                  <button type="button" className="button danger" onClick={() => void handleClearViews()}>
                    Очистить историю
                  </button>
                </div>

                {views.length === 0 ? <div className="empty">История пуста.</div> : null}
                <div className="list-cards">
                  {views.map((item) => (
                    <div key={`${item.id}-${item.name}`} className="card item-card">
                      <div className="item-card-header">
                        <div>
                          <h3>{item.name}</h3>
                          <p className="muted">{item.countryRu} · {item.type}</p>
                        </div>
                        <Link href={`/medicals/${item.id}`} className="button secondary">
                          Открыть
                        </Link>
                      </div>
                    </div>
                  ))}
                </div>
              </article>
            </section>
          </>
        ) : null}
      </div>
    </AppShell>
  );
}
