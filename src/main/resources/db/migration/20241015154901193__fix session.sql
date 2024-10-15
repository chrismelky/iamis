ALTER TABLE authorizations ALTER column authorization_code_metadata  type TEXT;
ALTER TABLE authorizations ALTER column access_token_metadata type TEXT;
ALTER TABLE authorizations ALTER column refresh_token_metadata type TEXT;
ALTER TABLE authorizations ALTER column oidc_id_token_metadata type TEXT;
ALTER TABLE authorizations ALTER column  oidc_id_token_claims type TEXT;
ALTER TABLE authorizations ALTER column  user_code_metadata type TEXT;
ALTER TABLE authorizations ALTER column  device_code_metadata type TEXT;