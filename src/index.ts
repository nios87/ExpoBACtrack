import {
  NativeModulesProxy,
  EventEmitter,
  Subscription,
} from "expo-modules-core";

// Import the native module. On web, it will be resolved to ExpoBacTrack.web.ts
// and on native platforms to ExpoBacTrack.ts
import {
  BacTrackConnectionEventPayload,
  ChangeEventPayload,
} from "./ExpoBacTrack.types";
import ExpoBacTrackModule from "./ExpoBacTrackModule";

// Get the native constant value.
export const PI = ExpoBacTrackModule.PI;

export function hello(): string {
  return ExpoBacTrackModule.hello();
}

export function init(): string {
  return ExpoBacTrackModule.init();
}

export function connectToNearest(): string {
  return ExpoBacTrackModule.connectToNearest();
}

export async function setValueAsync(value: string) {
  return await ExpoBacTrackModule.setValueAsync(value);
}

// const emitter = new EventEmitter(ExpoBacTrackModule);
const emitter = new EventEmitter(
  ExpoBacTrackModule ?? NativeModulesProxy.ExpoBacTrack
);

// export function addChangeListener(
//   listener: (event: ChangeEventPayload) => void
// ): Subscription {
//   return emitter.addListener<ChangeEventPayload>("onChange", listener);
// }

export function addChangeListener(
  listener: (event: ChangeEventPayload) => void
): Subscription {
  return emitter.addListener<ChangeEventPayload>("onChange", listener);
}

export function addBACtrackConnectionListener(
  listener: (event: BacTrackConnectionEventPayload) => void
): Subscription {
  return emitter.addListener<BacTrackConnectionEventPayload>(
    "onBacTrackConnection",
    listener
  );
}

export type ThemeChangeEvent = {
  theme: string;
};

export function addThemeListener(
  listener: (event: ThemeChangeEvent) => void
): Subscription {
  return emitter.addListener<ThemeChangeEvent>("onChangeTheme", listener);
}

export function getTheme(): string {
  return ExpoBacTrackModule.getTheme();
}

export function setTheme(theme: string): void {
  return ExpoBacTrackModule.setTheme(theme);
}

export { ChangeEventPayload, BacTrackConnectionEventPayload };
