/*
 * Copyright 2015-2021 Ritense BV, the Netherlands.
 *
 * Licensed under EUPL, Version 1.2 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

INSERT INTO accounts_user VALUES (1, 'pbkdf2_sha256$150000$804RI2AKro7g$4bToWwzuZO7OtruzIi6VRshCwshQVgxFTwF7ZOYowRg=', '2021-09-22 21:28:40.15254+00', true, 'admin', '', '', 'admin@exmaple.com', true, true, '2021-09-22 14:13:43.383088+00');

INSERT INTO authorizations_applicatie VALUES (1, '003aa834-4d24-4335-80aa-9a57555dcbe8', '{objectsapi}', 'ObjectsAPI', true);
INSERT INTO authorizations_applicatie VALUES (2, '1f5cb913-caa3-433b-851d-9a371a0c93a8', '{valtimo}', 'Valtimo', true);

INSERT INTO authorizations_authorizationsconfig VALUES (1, 'http://openzaak:8000/autorisaties/api/v1/', 'nrc');

INSERT INTO notifications_notificationsconfig VALUES (1, 'http://host.docker.internal:8002/api/v1/');

INSERT INTO vng_api_common_apicredential VALUES (1, 'http://openzaak:8000/autorisaties/api/v1/', 'opennotificaties', 'opennotificaties', 'openzaak', 'opennotificaties', 'opennotificaties');

INSERT INTO vng_api_common_jwtsecret VALUES (1, 'objectsapi', '9wu2''''Z`[x(eEk![:.$mBT6&:F!,%)EY74TS.AU+6');
INSERT INTO vng_api_common_jwtsecret VALUES (2, 'valtimo', 'zZ!xRP&$qTn4A9ETa^ZMKepDm^8egjPz');

INSERT INTO datamodel_kanaal VALUES(1, 'e069e3f0-6d5b-40ac-91fb-ad16384c5851', 'objecten', '', '{}');

SELECT pg_catalog.setval('datamodel_kanaal_id_seq', 1, true);