export interface DataModel {
  id: string;
  name: string;
  description: string;
  tags: string[];
  storageInfo: {
    storageType: string;
  };
  domain: string;
  updatedAt: string;
  creator: string;
  statInfo: {
    access: number;
    rating: number;
    favorite: number;
    download: number;
  };
  downloadInfo: {
    status: number;
    uri: string;
  };
}
