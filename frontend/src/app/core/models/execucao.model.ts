import { ServidorSgbd } from "./servidorSgbd.model";

export interface ExecucaoRequest {
  apelido: string;
  tipoExecucao: 'QUERY' | 'SCRIPT';
  idServidor: number;
  conteudoSql: string;
}

export interface ExecucaoResponse {
  idExecucao: number;
  apelido: string;
  usuarioExecutor: string;
  dtInicio: string;
  dtFIm: string;
  status: 'EM_ANDAMENTO' | 'CONCLUIDO' | 'ERRO' | 'CANCELADO';
  tipoExecucao: string;
  servidor?: Partial<ServidorSgbd>;
  conteudo: string;
}