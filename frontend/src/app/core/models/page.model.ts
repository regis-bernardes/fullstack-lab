export interface RestPage<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number; // página atual (0-indexada)
  size: number;
  first: boolean;
  last: boolean;
}