<!-- para IA. não é README de humano. -->
# SPEC — login

status: v0.3
sha: `c1ae7fc`
data: 2026-08-28

## Como usar
- Este arquivo é a fonte. Código ≠ spec → **bug de código**. Spec errada → Ricardo muda **este** arquivo, depois o código.
- IDs estáveis (`REGRA-` `DADOS-` `CONTRATO-` `NÃO-` `GAP-`). Não apague ID; marque `revogado`.
- "achei bug" → cita REGRA/CONTRATO. Se não existir, é GAP, não patch.
- "não estamos salvando X" → olha DADOS. Campo ausente = não é bug.
- "cadastrar campo X" → conflita se quebra REGRA/NÃO; senão vira GAP e só então código.
- GAP = pergunta aberta. Não trate GAP como regra.

## Papel
MS **interno** (porta `8082`). Emite token UUID **depois** que o user MS autenticou por email+código. Não é sessão: o cache de sessão está no **firewall**. Sem auth HTTP. Ver firewall `REGRA-EDGE-1`.

## REGRA
- REGRA-TOKEN-1: token emitido é UUID. `tokenType=bearer` **não** é contrato (JWT é legado).
- REGRA-TOKEN-2: resposta de create **não inclui senha** (não há senha).
- REGRA-TOKEN-3: login não persiste token em DB. Sem tabela de sessão.
- REGRA-CREDS-1: **revogado** (2026-08-28). Não valida email+senha. Não chama `/user/v1/retrieve/byEmailAndPassword`.
- REGRA-TOKEN-4: emite UUID para um user **já autenticado por código** (entra `email` e/ou `userId`). Sem senha no request.

## NÃO
- NÃO-JWT: não emite JWT.
- NÃO-EXPOSE: não é API pública.
- NÃO-SHUTDOWN: spec não inclui `/shutdown`.
- NÃO-PASSWORD: este MS não recebe nem confere senha.

## DADOS
Nenhum persistido. TokenVO de passagem: `token`, `email`, `createdIn`, `tokenType`, `userId` (do user MS). Sem senha.

## CONTRATO
- CONTRATO-CREATE `/login/v1/createToken` — entra `email` e/ou `userId` **sem senha**; sai UUID **cru** (sem `Bearer`). Quem prefixa é o cliente/borda. Chamado pela borda **depois** de CONTRATO-EV-OK no user MS. código alinhado (sem senha; produto emite UUID no cache da borda).
- CONTRATO-HEALTH `/login/v1/healthCheck` `/healthCheck` `/test`

Não há `/login/v1/validateToken` usado pela borda atual (firewall valida o próprio cache).

## Fala com
user `:8083` — só para obter `userId` se a borda mandar só email. **Não** `byEmailAndPassword`. URL cravada no código é GAP-URL (ainda aponta o retrieve de senha).

## GAP
- GAP-JWT: **revogado** (PR login #10). Spec: recusar `tokenType=bearer`.
- GAP-URL: usar `MicroservicesURLs` vs localhost cravado; apontar para retrieve por email (sem senha), não `byEmailAndPassword`.
