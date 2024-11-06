import * as ExpoBacTrack from "expo-bac-track";
import { BacTrackConnectionEventPayload } from "expo-bac-track/ExpoBacTrack.types";
import { useEffect, useState } from "react";
import {
  Button,
  PermissionsAndroid,
  StyleSheet,
  Text,
  View,
} from "react-native";

export default function App() {
  const [connectionStatus, setConnectionStatus] =
    useState<BacTrackConnectionEventPayload | null>(null);

  const [connectionStatus2, setConnectionStatus2] = useState<string>("null");
  const [theme, setTheme] = useState<string>(ExpoBacTrack.getTheme());

  const nextTheme = theme === "dark" ? "light" : "dark";
  useEffect(() => {
    console.log("subscribe to event");
    const subscription = ExpoBacTrack.addChangeListener(({ value: test }) => {
      console.log("EVENT EMITTED", test);
      setConnectionStatus2(test);
    });

    return () => {
      console.log("remove listener");
      subscription.remove();
    };
  }, [setConnectionStatus]);

  useEffect(() => {
    const subscription = ExpoBacTrack.addThemeListener(
      ({ theme: newTheme }) => {
        setTheme(newTheme);
      }
    );

    return () => subscription.remove();
  }, [setTheme]);

  useEffect(() => {
    const subscription = ExpoBacTrack.addBACtrackConnectionListener((data) => {
      console.log("bacTrack connection update", data);
    });

    return () => subscription.remove();
  }, []);

  // const register = () => {
  //   console.log("register device");
  //   const subscription = ExpoBacTrack.addChangeListener(({ value: test }) => {
  //     console.log("EVENT EMITTED", test);
  //     setConnectionStatus(connectionStatus);
  //   });
  // };

  return (
    <View style={styles.container}>
      <View style={{ padding: 20 }}>
        <Text>BACtrack Connection Status: {connectionStatus2}</Text>
        {connectionStatus ? (
          <View>
            <Text>Connecting: {connectionStatus.mIsConnecting}</Text>
            <Text>Connected: {connectionStatus.mIsConnected}</Text>
            <Text>Scanning: {connectionStatus.mIsScanning}</Text>
            <Text>Battery Level: {connectionStatus.mBatteryLevel}</Text>
          </View>
        ) : (
          <Text>No data yet...</Text>
        )}
      </View>
      <View style={{ flex: 1, alignItems: "center", justifyContent: "center" }}>
        <Text>Theme: {ExpoBacTrack.getTheme()}</Text>
        <Button
          title={`Set theme to ${nextTheme}`}
          onPress={() => {
            ExpoBacTrack.setTheme(nextTheme);
            const theme = ExpoBacTrack.getTheme();
            console.log("current Theme", theme);
          }}
        />
      </View>
      <Button title="Init" onPress={() => ExpoBacTrack.init()} />

      <Button
        title="setValueasync"
        onPress={() => ExpoBacTrack.setValueAsync("test")}
      />

      {/* <Button title="register" onPress={() => register()} /> */}

      <Button
        title="Connect to nearest"
        onPress={() => ExpoBacTrack.connectToNearest()}
      />

      <Button
        title="permission coarse location"
        onPress={() =>
          PermissionsAndroid.request(
            PermissionsAndroid.PERMISSIONS.ACCESS_COARSE_LOCATION
          )
            .then((res) => console.log("permissions res", res))
            .catch((e) => console.log("permissions error", e))
        }
      />
      <Button
        title="permission fine location"
        onPress={() =>
          PermissionsAndroid.request(
            PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION
          )
            .then((res) => console.log("permissions res", res))
            .catch((e) => console.log("permissions error", e))
        }
      />
      <Button
        title="permission BLE"
        onPress={() =>
          PermissionsAndroid.requestMultiple([
            PermissionsAndroid.PERMISSIONS.BLUETOOTH_SCAN,
            PermissionsAndroid.PERMISSIONS.BLUETOOTH_ADVERTISE,
            PermissionsAndroid.PERMISSIONS.BLUETOOTH_CONNECT,
          ])
            .then((res) => console.log("permissions res", res))
            .catch((e) => console.log("permissions error", e))
        }
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "space-between",
  },
  button: {
    padding: 16,
  },
});
