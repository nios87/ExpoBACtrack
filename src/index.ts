import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';

// Import the native module. On web, it will be resolved to ExpoBacTrack.web.ts
// and on native platforms to ExpoBacTrack.ts
import ExpoBacTrackModule from './ExpoBacTrackModule';
import ExpoBacTrackView from './ExpoBacTrackView';
import { ChangeEventPayload, ExpoBacTrackViewProps } from './ExpoBacTrack.types';

// Get the native constant value.
export const PI = ExpoBacTrackModule.PI;

export function hello(): string {
  return ExpoBacTrackModule.hello();
}

export async function setValueAsync(value: string) {
  return await ExpoBacTrackModule.setValueAsync(value);
}

const emitter = new EventEmitter(ExpoBacTrackModule ?? NativeModulesProxy.ExpoBacTrack);

export function addChangeListener(listener: (event: ChangeEventPayload) => void): Subscription {
  return emitter.addListener<ChangeEventPayload>('onChange', listener);
}

export { ExpoBacTrackView, ExpoBacTrackViewProps, ChangeEventPayload };
