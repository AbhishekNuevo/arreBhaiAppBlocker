import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, FlatList, TouchableOpacity, NativeModules } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import { appsName, socialMediaWebsites } from './appsName';

const ApBlockScreen: React.FC = () => {
  const [blockedItems, setBlockedItems] = useState<{ [key: string]: boolean }>({});
  const { SharedPreferencesModule } = NativeModules;
  const data = [
    { title: 'Apps', data: appsName, color: '#E6F3FF' },
    { title: 'Social Media Websites', data: socialMediaWebsites, color: '#E6F3DF' },
  ];

  useEffect(() => {
    const fetchBlockedApps = async () => {
      try {
        const blockedAppsString = await SharedPreferencesModule.getValue('blockedApps');
        if (blockedAppsString) {
          const blockedApps = JSON.parse(blockedAppsString);
          const newBlockedItems: { [key: string]: boolean } = {};
          blockedApps.forEach((app: any) => {
            newBlockedItems[app.id] = true;
          });
          setBlockedItems(newBlockedItems);
        }
      } catch (error) {
        console.error('Error fetching blocked apps:', error);
      }
    };

    fetchBlockedApps();
  }, []);

  const toggleBlock = (itemId: string) => {
    setBlockedItems(prev => ({
      ...prev,
      [itemId]: !prev[itemId],
    }));
  };

  useEffect(() => {
    const updateBlockedApps = async () => {
      let blockedApps = [];
      for (let item in blockedItems) {
        if (blockedItems[item]) {
          const appIndex = appsName?.findIndex(app => app?.id.toString() === item);
          const siteIndex = socialMediaWebsites?.findIndex(app => app?.id.toString() === item);
          if (appIndex > -1) {
            blockedApps.push(appsName[appIndex]);
          }
          if (siteIndex > -1) {
            blockedApps.push(socialMediaWebsites[siteIndex]);
          }
        }
      }

      try {
        if (blockedApps?.length > 0) {
          await SharedPreferencesModule?.setValue('blockedApps', JSON.stringify(blockedApps));
        } else {
          await SharedPreferencesModule?.setValue('blockedApps', '');
        }
      } catch (error) {
        console.error('Error updating blocked apps:', error);
      }
    };

    updateBlockedApps();
  }, [blockedItems]);

  const renderItem = ({ item, color }: { item: any; color: string }) => {
    const isBlocked = blockedItems[item.id];
    return (
      <View key={item?.id} style={[styles.itemContainer, { backgroundColor: isBlocked ? '#FFE6E6' : color }]}>
        <Icon name={item.icon} size={24} color="#4A4A4A" />
        <Text style={styles.itemText}>{item.name}</Text>
        <TouchableOpacity
          style={[
            styles.blockButton,
            { backgroundColor: isBlocked ? '#FFE6E6' : color },
          ]}
          onPress={() => toggleBlock(item.id)}
        >
          <Text style={[styles.blockButtonText, { color: isBlocked ? '#4A4A4A' : '#4A4A4A' }]}>
            {isBlocked ? 'Unblock' : 'Block'}
          </Text>
        </TouchableOpacity>
      </View>
    );
  };

  const renderSectionHeader = ({ section: { title } }: { section: { title: string; color: string } }) => (
    <Text style={styles.sectionHeader}>{title}</Text>
  );

  return (
    <View style={styles.container}>
      <FlatList
        data={data}
        renderItem={({ item, index }) => (
          <View key={index}>
            {renderSectionHeader({ section: { title: item.title, color: item.color } })}
            <View>
               {item.data.map((subItem: any) => renderItem({ item: subItem, color: item.color }))}
            </View>
          </View>
        )}
        showsVerticalScrollIndicator={false}
        keyExtractor={(item, index) => index.toString()}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    backgroundColor: 'white',
  },
  sectionHeader: {
    fontSize: 24,
    fontWeight: 'bold',
    paddingHorizontal: 10,
    color: '#333',
    marginBottom: 10,
  },
  itemContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 12,
    borderRadius: 8,
    marginBottom: 8,
  },
  itemText: {
    fontSize: 18,
    marginLeft: 10,
    flex: 1,
    color: '#4A4A4A',
  },
  blockButton: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 6,
    borderWidth: 1,
    borderColor: '#4A4A4A',
  },
  blockedButton: {
    borderColor: '#FF0000', // Changed to a danger color (red)
  },
  blockButtonText: {
    fontWeight: 'bold',
  },
});

export default ApBlockScreen;
