export function formatDate(value?: string): string {
  if (!value) return "—";
  try {
    return new Intl.DateTimeFormat("ru-RU", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
    }).format(new Date(value));
  } catch {
    return value;
  }
}

export function formatDateTime(value?: string): string {
  if (!value) return "—";
  try {
    return new Intl.DateTimeFormat("ru-RU", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
    }).format(new Date(value));
  } catch {
    return value;
  }
}

export function boolLabel(value?: boolean): string {
  return value ? "Да" : "Нет";
}

export function normalizeMultiline(value: string): string[] {
  return value
    .split(/\n|,/)
    .map((item) => item.trim())
    .filter(Boolean);
}

export function toMultiline(values?: string[]): string {
  return values?.join("\n") ?? "";
}

export function cx(...values: Array<string | false | null | undefined>): string {
  return values.filter(Boolean).join(" ");
}

export function getInitials(fullName: string): string {
  return fullName
    .split(" ")
    .filter(Boolean)
    .slice(0, 2)
    .map((part) => part[0]?.toUpperCase() ?? "")
    .join("");
}
