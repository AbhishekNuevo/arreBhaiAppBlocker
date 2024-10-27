/**
 * Simple Home Screen for ArreBhai App
 *
 * @format
 */

import React, { useEffect, useState } from 'react';
import { DeviceEventEmitter } from 'react-native';
import {
  SafeAreaView,
  StyleSheet,
  useColorScheme,
} from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import Icon from 'react-native-vector-icons/Ionicons';

import {Colors} from 'react-native/Libraries/NewAppScreen';
import HomeScreen from './src/HomeScreen';
import SettingsScreen from './src/SettingsScreen';
import ApBlockScreen from './src/ApBlockScreen';
import BlockScreen from './src/blockScreen';
import { AppContent } from './src/content';

const getTabBarIcon = (route: string, focused: boolean, color: string, size: number) => {
  let iconName: string;

  if (route === 'Home') {
    iconName = focused ? 'home' : 'home-outline';
  } else if (route === 'Settings') {
    iconName = focused ? 'settings' : 'settings-outline';
  } else if (route === 'ApBlock') {
    iconName = focused ? 'shield' : 'shield-outline';
  } else {
    iconName = 'alert-circle'; // Default icon
  }

  return <Icon name={iconName} size={size} color={color} />;
};

function App(): React.JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };
  const Tab = createBottomTabNavigator();

  const [eventMessage, setEventMessage] = useState('');


  useEffect(() => {
    console.log('Accessibility event package name:111',);
    const subscription = DeviceEventEmitter.addListener('onAccessibilityEvent', (event) => {
      console.log('Accessibility event package name:', JSON.stringify(event));
      // Handle the package name or perform actions based on the event
      setEventMessage(AppContent.hindi.messageScold);
      });

  // Cleanup listener on unmount
     return () => {
      // setEventMessage('');
      subscription.remove();
     };
  }, []);


  return (
    <SafeAreaView style={[styles.container, backgroundStyle, styles.safeArea] }>
      <NavigationContainer>
        <Tab.Navigator
          screenOptions={({ route }) => ({
            tabBarIcon: ({ focused, color, size }) => getTabBarIcon(route.name, focused, color, size),
          })}
        >
          <Tab.Screen name="Home" component={HomeScreen} />
          <Tab.Screen name="ApBlock" component={ApBlockScreen} />
          <Tab.Screen name="Settings" component={SettingsScreen} />

        </Tab.Navigator>
      </NavigationContainer>
          <BlockScreen eventMessage={eventMessage} setEventMessage={setEventMessage} />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'white',
  },
  safeArea: {
    backgroundColor: 'white',
  },
  content: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  title: {
    fontSize: 28,
    fontWeight: 'bold',
    marginBottom: 16,
    textAlign: 'center',
  },
  subtitle: {
    fontSize: 18,
    textAlign: 'center',
  },

});

export default App;
