import { StyleSheet, Text, View } from 'react-native';

import * as ExpoBacTrack from 'expo-bac-track';

export default function App() {
  return (
    <View style={styles.container}>
      <Text>{ExpoBacTrack.hello()}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
