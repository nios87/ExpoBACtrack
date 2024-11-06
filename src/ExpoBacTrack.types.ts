export type ChangeEventPayload = {
  value: string;
};

export type BacTrackConnectionEventPayload = {
  mIsConnected: boolean;
  mBatteryLevel: number;
  mIsConnecting: boolean;
  mIsScanning: boolean;
};

