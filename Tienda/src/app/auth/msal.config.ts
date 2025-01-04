import { LogLevel, Configuration, BrowserCacheLocation } from '@azure/msal-browser';

const tenantName = 'DCNGP6';
const tenantId = 'a003d17b-11dd-4217-84fc-460e5409dc74';
const policyName = 'B2C_1_DCNGP6_LOGIN';  // El nombre exacto de tu policy

// Endpoints para Azure AD B2C
const b2cPolicies = {
  authorities: {
    signUpSignIn: {
      authority: `https://${tenantName}.b2clogin.com/${tenantName}.onmicrosoft.com/${policyName}`,
    }
  },
  authorityDomain: `${tenantName}.b2clogin.com`
};

export const msalConfig: Configuration = {
  auth: {
    clientId: '817eaf26-9869-4d26-8e71-d2713298ddd7',
    authority: b2cPolicies.authorities.signUpSignIn.authority,
    knownAuthorities: [b2cPolicies.authorityDomain],
    redirectUri: 'http://localhost:4200/login',
    postLogoutRedirectUri: 'http://localhost:4200',
    navigateToLoginRequestUrl: true
  },
  cache: {
    cacheLocation: BrowserCacheLocation.LocalStorage,
    storeAuthStateInCookie: false,
  },
  system: {
    allowNativeBroker: false,
    loggerOptions: {
      loggerCallback: (level: LogLevel, message: string, containsPii: boolean): void => {
        if (containsPii) {
          return;
        }
        switch (level) {
          case LogLevel.Error:
            console.error(message);
            return;
          case LogLevel.Info:
            console.info(message);
            return;
          case LogLevel.Verbose:
            console.debug(message);
            return;
          case LogLevel.Warning:
            console.warn(message);
            return;
        }
      },
      logLevel: LogLevel.Verbose,
      piiLoggingEnabled: false
    }
  }
};

export const loginRequest = {
  scopes: ['openid', 'profile', 'offline_access']
};

export const protectedResources = {
  api: {
    endpoint: "http://localhost:8081/api",
    scopes: [`https://${tenantName}.onmicrosoft.com/api/user_impersonation`],
  },
};