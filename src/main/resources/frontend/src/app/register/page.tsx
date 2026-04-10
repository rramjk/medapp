"use client";

import Link from "next/link";
import { useState } from "react";
import { AppShell } from "@/components/app-shell";
import { PageHeader } from "@/components/page-header";
import { UserEditorForm } from "@/components/user-editor-form";
import { api } from "@/lib/api";
import type { UserDto, UserRequestDto } from "@/lib/types";

export default function RegisterPage() {
  const [createdUser, setCreatedUser] = useState<UserDto | null>(null);

  async function handleCreate(payload: UserRequestDto) {
    const response = await api.users.create(payload);
    setCreatedUser(response);
  }

  return (
    <AppShell>
      <div className="page">
        <PageHeader
          title="Регистрация"
          description="Публичная форма создания пользователя через POST /api/users."
          actions={
            <Link href="/login" className="button secondary">
              Уже есть аккаунт
            </Link>
          }
        />

        <UserEditorForm
          mode="create"
          submitLabel="Создать пользователя"
          note="Форма отправляет данные точно в структуре UserRequestDto. После регистрации можно сразу перейти к авторизации."
          onSubmit={handleCreate}
        />

        {createdUser ? (
          <section className="card">
            <h3>Пользователь создан</h3>
            <div className="kv">
              <div className="kv-row">
                <dt>ID</dt>
                <dd>{createdUser.id}</dd>
              </div>
              <div className="kv-row">
                <dt>Email</dt>
                <dd>{createdUser.email}</dd>
              </div>
              <div className="kv-row">
                <dt>Роль</dt>
                <dd>{createdUser.roleName}</dd>
              </div>
            </div>
            <div className="inline-actions">
              <Link href="/login" className="button">
                Перейти ко входу
              </Link>
            </div>
          </section>
        ) : null}
      </div>
    </AppShell>
  );
}
