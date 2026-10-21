import { ServidorSgbd } from "./servidorSgbd.model";
import { ExecucaoResponse } from "./execucao.model";

export interface ResultadoTabela {
  colunas: string[];
  linhas: Record<string, any>[];
}

export interface ResultadoExecucaoResponse {
idResultadoExecucao: number;
execucao : Partial<ExecucaoResponse>;
servidor?: Partial<ServidorSgbd>;
tipoExecucao: string;
conteudo: string;
dtInicio: string;
dtFIm: string;
status: 'EM_ANDAMENTO' | 'CONCLUIDO' | 'ERRO' | 'CANCELADO';
linhasAfetadas: number;
mensagemErro: string;
resultados: string;
    
}