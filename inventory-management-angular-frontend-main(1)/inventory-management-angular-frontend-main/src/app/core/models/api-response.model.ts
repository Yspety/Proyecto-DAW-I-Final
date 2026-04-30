export interface ApiEnvelope<T> {
  success: boolean;
  message: string;
  data: T;
  total?: number;
}
