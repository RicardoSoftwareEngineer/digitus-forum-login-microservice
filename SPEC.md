<!-- para IA. não é README de humano. -->
# SPEC — login

status: v0.2
sha: `0cec5b6`
data: 2026-08-28

## Como usar
- Este arquivo é a fonte. Código ≠ spec → **bug de código**. Spec errada → Ricardo muda **este** arquivo, depois o código.
- IDs estáveis (`REGRA-` `DADOS-` `CONTRATO-` `NÃO-` `GAP-`). Não apague ID; marque `revogado`.
- "achei bug" → cita REGRA/CONTRATO. Se não existir, é GAP, não patch.
- "não estamos salvando X" → olha DADOS. Campo ausente = não é bug.
- "cadastrar campo X" → conflita se quebra REGRA/NÃO; senão vira GAP e só então código.
- GAP = pergunta aberta. Não trate GAP como regra.

## Papel
MS **interno** (porta `8082`). Emite token UUID. Não é sessão: o cache de sessão está no **firewall**. Sem auth HTTP (desenho: não expor na internet). Ver firewall `REGRA-EDGE-1`.

## REGRA
- REGRA-TOKEN-1: token emitido é UUID. `tokenType=bearer` **não** é contrato (JWT é legado).
- REGRA-TOKEN-2: resposta de create **não inclui senha**.
- REGRA-TOKEN-3: login não persiste token em DB. Sem tabela de sessão.
- REGRA-CREDS-1: valida email+senha no MS user (`/user/v1/retrieve/byEmailAndPassword`).

## NÃO
- NÃO-JWT: não emite JWT. (código ainda tem ramo bearer — GAP / PR #10)
- NÃO-EXPOSE: não é API pública.
- NÃO-SHUTDOWN: spec não inclui `/shutdown`.

## DADOS
Nenhum persistido. TokenVO de passagem: `token`, `email`, `createdIn`, `tokenType`, `userId` (se vier do user MS). Senha só no request, nunca no response.

## CONTRATO
- CONTRATO-CREATE `/login/v1/createToken` — entra email+senha; sai UUID **cru** (sem `Bearer`). Quem prefixa é o cliente/borda.
- CONTRATO-HEALTH `/login/v1/healthCheck` `/healthCheck` `/test`

Não há `/login/v1/validateToken` usado pela borda atual (firewall valida o próprio cache).

## Fala com
user `:8083` — no código a URL de senha está **cravada** (`http://localhost:8083/user/v1/retrieve/byEmailAndPassword`), não via `MicroservicesURLs`. GAP-URL.

## GAP
- GAP-JWT: `createToken` ainda aceita bearer (PR login #10). Spec: recusar.
- GAP-URL: usar `MicroservicesURLs` vs localhost cravado.
