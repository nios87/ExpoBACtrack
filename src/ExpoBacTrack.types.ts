export type ChangeEventPayload = {
  value: string;
};

export type BacTrackConnectionEventPayload = {
  mIsConnected: boolean;
  mBatteryLevel: number;
  mIsConnecting: boolean;
  mIsScanning: boolean;
};

export type BacTrackBlowPayload = {
  remaining: number;
};

export type BacTrackCountdownPayload = {
  count: number;
};
export type BacTrackResultPayload = {
  result: number;
};
