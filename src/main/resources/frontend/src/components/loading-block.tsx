export function LoadingBlock({ label = "Загрузка данных…" }: { label?: string }) {
  return <div className="loading-block">{label}</div>;
}
