"use client";

import Link from "next/link";
import { useState } from "react";
import { AppShell } from "@/components/app-shell";
import { PageHeader } from "@/components/page-header";
import { UserEditorForm } from "@/components/user-editor-form";
import { api } from "@/lib/api";
import type { UserDto, UserRequestDto } from "@/lib/types";

export default function CreateUserPage() {
  const [created, setCreated] = useState<UserDto | null>(null);

  async function handleCreate(payload: UserRequestDto) {
    const response = await api.users.create(payload);
    setCreated(response);
  }

  return (
    <AppShell>
      <div className="page">
        <PageHeader
          title="Создать пользователя"
          description="Отдельный административный экран для сценария POST /api/users."
          actions={
            <Link href="/users" className="button secondary">
              Назад к списку
            </Link>
          }
        />

        <UserEditorForm mode="create" submitLabel="Создать пользователя" onSubmit={handleCreate} />

        {created ? (
          <section className="card">
            <h3>Результат</h3>
            <div className="kv">
              <div className="kv-row"><dt>ID</dt><dd>{created.id}</dd></div>
              <div className="kv-row"><dt>Email</dt><dd>{created.email}</dd></div>
              <div className="kv-row"><dt>Role</dt><dd>{created.roleName}</dd></div>
            </div>
          </section>
        ) : null}
      </div>
    </AppShell>
  );
}
