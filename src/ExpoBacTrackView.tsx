import { requireNativeViewManager } from 'expo-modules-core';
import * as React from 'react';

import { ExpoBacTrackViewProps } from './ExpoBacTrack.types';

const NativeView: React.ComponentType<ExpoBacTrackViewProps> =
  requireNativeViewManager('ExpoBacTrack');

export default function ExpoBacTrackView(props: ExpoBacTrackViewProps) {
  return <NativeView {...props} />;
}
