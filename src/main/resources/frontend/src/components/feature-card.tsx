interface FeatureCardProps {
  title: string;
  description: string;
  badge?: string;
}

export function FeatureCard({ title, description, badge }: FeatureCardProps) {
  return (
    <div className="card feature-card">
      {badge ? <span className="badge">{badge}</span> : null}
      <h3>{title}</h3>
      <p className="muted">{description}</p>
    </div>
  );
}
