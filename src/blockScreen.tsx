/**
 * @fileoverview Block screen component
 * @module BlockScreen
 */
import RNExitApp from 'react-native-exit-app';
import React, { useState } from 'react';
import { View, Text, Modal, TouchableOpacity, StyleSheet,NativeModules} from 'react-native';
import { AppContent } from './content';


const unlockTimeArray = [10, 30, 60, 120, 300];

const BlockScreen = (props:any) => {
  const { SharedPreferencesModule } = NativeModules;
    const {eventMessage, setEventMessage} = props || {};
    const [unlock, setUnlock] = useState(false);

    const closeApp = () => {
        setEventMessage('');
        // RNExitApp.exitApp();
    };

    const unlockView = () => {
      return  (<View style={styles.unlockContainer} >
      { unlockTimeArray?.map(item => (
        <TouchableOpacity onPress={() => setUnlockTime(item)} activeOpacity={0.5} key={item} style={styles.unlockButton}>
          <Text>{item} sec</Text>
        </TouchableOpacity>
      ))}

      </View>);
    };

    const unlockViewHandler = () => {
      setUnlock(value => !value);
    };

    const setUnlockTime = (item:any) => {
      const timeStamp = Date.now() + item * 1000;
      SharedPreferencesModule?.setValue('unlockTime', timeStamp?.toString());
      setUnlock(false);
    };

  return (
    <Modal
    animationType="fade"
    transparent={false}
    visible={eventMessage?.length > 0}
    onRequestClose={() => {
      setEventMessage('');
    }}
  >
    <View style={styles.modalView}>
      <View style={styles.modalTextContainer}>
        <Text style={styles.modalText}>{AppContent?.hindi?.messageScold}</Text>
        <Text style={styles.modalTextSmall}>{AppContent?.hindi?.messageAdvice}</Text>
      </View>
      <TouchableOpacity activeOpacity={0.5} style={styles.unlockViewBtn} onPress={() => unlockViewHandler() }>
        <Text style={styles.modalTextSmall2}>Unlock</Text>
      </TouchableOpacity>
      { unlock && unlockView()}
      <TouchableOpacity
      activeOpacity={0.5}
        style={styles.buttonClose}
        onPress={() => closeApp()}
      >

        <Text style={styles.textStyle}>Close</Text>
      </TouchableOpacity>
    </View>
  </Modal>

  );
};

export default BlockScreen;


const styles = StyleSheet.create({
    modalView: {
        flex: 1,
        width: '100%',
        height: '100%',
        backgroundColor: 'white',
        justifyContent: 'center',
        alignItems: 'center',
        padding: 35,
      },
      modalText: {
        marginBottom: 15,
        textAlign: 'center',
        fontSize: 22,
        fontWeight: 'bold',
        color: 'black',
      },
      modalTextSmall: {
        marginBottom: 15,
        textAlign: 'center',
        fontSize: 18,
        fontWeight: 'bold',
        color: 'black',
      },
      modalTextSmall2: {
        textAlign: 'center',
        fontSize: 18,
        fontWeight: 'bold',
        color: 'white',
      },
      buttonClose: {
        backgroundColor: '#2196F3',
        borderRadius: 20,
        padding: 10,
        elevation: 2,
        marginTop: 20,
        position: 'absolute',
        bottom: 40,
        width: '100%',
      },
      textStyle: {
        color: 'white',
        fontWeight: 'bold',
        textAlign: 'center',
      },

      modalTextContainer : {
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
      },

      unlockContainer : {
        flexDirection: 'row',
        // justifyContent: 'center',
        alignItems: 'center',
        width: '100%',
        flexWrap: 'wrap',
       borderColor:'#2196F3',
       borderWidth:1,
       borderRadius: 8,
        marginTop: 24,

      },

      unlockButton : {
        backgroundColor: '#2196F3',
        borderRadius: 8,
        padding: 12,
        elevation: 2,
        margin: 10,
      },

      unlockViewBtn : {
        backgroundColor: '#c8e0df',
        position: 'absolute',
        top: 40,
        left: 20,
        justifyContent: 'center',
        alignItems: 'center',
        padding: 10,
        borderRadius: 8,

      },
});
