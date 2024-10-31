import React, {useState} from 'react';
import {View, Text, StyleSheet, TouchableOpacity, Modal,TextInput} from 'react-native';
import DeviceInfo from 'react-native-device-info';

const SettingsScreen: React.FC = () => {
  const appVersion = DeviceInfo.getVersion();
  const [modalVisible, setModalVisible] = useState(false);

  return (
    <>
      <View style={styles.container}>
        <View style={styles.buttonContainer}>
          <TouchableOpacity
            // onPress={() => setModalVisible(true)}
            style={styles.button}
            activeOpacity={0.5}>
            <Text style={styles.title}>Add more apps to block</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.button} activeOpacity={0.5}>
            <Text style={styles.title}>Add more sites to block</Text>
          </TouchableOpacity>
        </View>
      </View>
      <View style={styles.appInfo}><Text style={{color:'gray'}}>{appVersion}</Text></View>

      <Modal animationType="slide" visible={modalVisible} transparent={true}>
        <View style={styles.modalContainer} />
        <View style={styles.modalViewContaienr}>
          <Text style={{color: 'black'}}>Enter the app name</Text>
          <TextInput style={styles.input} />
          <Text style={{color: 'black'}}>Enter the package name (bundle id)</Text>
          <TextInput style={styles.input} />
        </View>
      </Modal>
    </>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'flex-start',
    backgroundColor: 'white',
    padding: 24,
    width: '100%',
  },
  title: {
    fontSize: 16,
    fontWeight: 'bold',
    color: 'black',
  },
  subtitle: {
    fontSize: 14,
    textAlign: 'center',
  },

  buttonContainer: {
    flexDirection: 'column',
    gap: 16,
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'center',
    width: '100%',
  },
  button: {
    padding: 10,
    borderRadius: 10,
    backgroundColor: '#F5F5F5',
    width: '100%',
    justifyContent: 'center',
    alignItems: 'center',
  },
  modalContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    width: '100%',
    height: '70%',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
  },

  modalViewContaienr: {
    backgroundColor: 'white',
    width: '80%',
    height: '80%',
    position: 'absolute',
    top: '10%',
    left: '10%',
    alignItems: 'center',
    borderRadius: 16,
  },
  appInfo:{
    width:'100%',
    justifyContent:'center',
    alignItems:'center',
    position:'absolute',
    bottom:10,
  },


});

export default SettingsScreen;
