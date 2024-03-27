import * as React from 'react';

import { ExpoBacTrackViewProps } from './ExpoBacTrack.types';

export default function ExpoBacTrackView(props: ExpoBacTrackViewProps) {
  return (
    <div>
      <span>{props.name}</span>
    </div>
  );
}
