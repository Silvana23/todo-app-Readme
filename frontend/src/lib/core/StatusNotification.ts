export enum EStatusNotification {
  INFO,
  WARN,
  ERROR
}

export interface IStatusNotification {
  type: EStatusNotification
  message: string
}

export function createNotification(
  type: EStatusNotification,
  message: string
): IStatusNotification {
  return {
    type,
    message
  }
}

export function statusCodeToNotificationStatus(code: number): EStatusNotification {
  if (code >= 200 && code < 300) {
    return EStatusNotification.INFO
  } else if (code >= 300 && code < 500) {
    return EStatusNotification.WARN
  } else {
    return EStatusNotification.ERROR
  }
}
