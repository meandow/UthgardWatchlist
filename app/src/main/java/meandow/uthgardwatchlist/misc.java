        ZoneData instance = ZoneData.getInstance(getContext());
final int newMeshID = Integer.parseInt(edittext.getText().toString());
                        ZoneData instance = ZoneData.getInstance(getContext());
                        switch(viewID) {
                            case R.id.front_left:
                                instance.setMeshId((byte) newMeshID,0);
                                break;
                            case R.id.front_right:
                                instance.setMeshId((byte) newMeshID,1);
                                break;
                            case R.id.back_left:
                                instance.setMeshId((byte) newMeshID,2);
                                break;
                            case R.id.back_right:
                                instance.setMeshId((byte) newMeshID,3);
                                break;
                        }
                        instance.save(getContext());
                        
                        ZoneData instance = ZoneData.getInstance(getContext());
            byte meshId=0x00;
            int dimValue=0;
            if (mFrontLeft.isSelected()) {
                meshId = instance.Zones.get(selectedItem).TopLeft.MeshId;
                dimValue = mDimFrontLeft;
            } else if (mFrontRight.isSelected()) {
                meshId = instance.Zones.get(selectedItem).TopRight.MeshId;
                dimValue = mDimFrontRight;
            } else if (mBackLeft.isSelected()) {
                meshId = instance.Zones.get(selectedItem).BottomLeft.MeshId;
                dimValue = mDimBackLeft;
            } else if (mBackRight.isSelected()) {
                meshId = instance.Zones.get(selectedItem).BottomRight.MeshId;
                dimValue = mDimBackRight;
            }
            
                            instance.Zones.get(selectedItem).TopRight.Dim = (double) mDimFrontRight/100.0;
            instance.save(getContext());
