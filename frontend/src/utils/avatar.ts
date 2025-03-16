import { AVATAR_COLORS } from '@/utils/constants';

export const getAvatarColor = (name: string): string => {
  try {
    if (!name) return AVATAR_COLORS[0];
    let hash = 0;
    for (let i = 0; i < name.length; i++) {
      hash = name.charCodeAt(i) + ((hash << 5) - hash);
    }
    return AVATAR_COLORS[Math.abs(hash) % AVATAR_COLORS.length];
  } catch (error) {
    console.error('Error generating avatar color:', error);
    return AVATAR_COLORS[0];
  }
};

export const getAvatarText = (name: string): string => {
  try {
    if (!name) return '';
    const parts = name.split(' ');
    if (parts.length >= 2) {
      return (parts[0][0] + parts[1][0]).toUpperCase();
    }
    return name.slice(0, 2).toUpperCase();
  } catch (error) {
    console.error('Error generating avatar text:', error);
    return 'ERR';
  }
};
