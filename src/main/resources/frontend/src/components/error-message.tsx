export function ErrorMessage({ message }: { message?: string | null }) {
  if (!message) return null;

  return (
    <div className="alert alert-error" role="alert">
      <strong>Ошибка.</strong> {message}
    </div>
  );
}
