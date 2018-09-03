package com.swssm.waveloop.soundfile;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.content.res.AssetManager;

/**
 * A native MP3 {@link Decoder} based on libmad. Decodes
 * stereo input to mono. 
 * @author mzechner
 *
 */
public class NativeMP3Decoder implements Decoder 
{       
        /** the handle to the native mp3 decoder **/
        private int handle;
        
        /** the float buffer used to read in the samples **/
        private FloatBuffer buffer;
        
        /** the short buffer **/
        private ShortBuffer[] shortBuffer;
        
        /**
         * Constructor, sets the file to decode. The file is given in absolute terms! FIXME
         *
         * @param file The file.
         */
        
        
        
        
        public NativeMP3Decoder(AssetManager assetManager, String file )
        {
                handle = openFile(assetManager, file );
                if( handle == -1 )
                        throw new IllegalArgumentException( "Couldn't open file '" + file + "'" );
        }
                
        private native int openFile(AssetManager assetManager, String file );
        
        /**
         * Reads in numSamples float PCM samples to the provided direct FloatBuffer using the
         * handle retrievable via {@link NativeMP3Decoder.getHandle()}. This is for 
         * people who know what they do. Returns the number of samples actually read.
         * 
         * @param handle The handle
         * @param buffer The direct FloatBuffer
         * @param numSamples The number of samples to read
         * @return The number of samples read.
         */
        public native int readSamples( int handle, FloatBuffer buffer, int numSamples );

        /**
         * Reads in numSamples 16-bit signed PCM samples to the provided direct FloatBuffer using the
         * handle retrievable via {@link NativeMP3Decoder.getHandle()}. This is for 
         * people who know what they do. Returns the number of samples actually read.
         * 
         * @param handle The handle
         * @param buffer The direct FloatBuffer
         * @param numSamples The number of samples to read
         * @return The number of samples read.
         */
        public native int readSamples( int handle, ShortBuffer bufferL,ShortBuffer bufferR, int numSamples );
        
        private native void closeFile( int handle );
        
        private native float getProgress( int handle );
        
        
        
        // 1/50초에 해당하는 파형값을 가져옴
        public native int readSamplesAll( int handle );  
        
        /**
         * @return The handle retrieved from the native side.
         */
        public int getHandle( )
        {
                return handle;
        }
        
        /**
         * {@inheritDoc}
         */
        public int readSamples(float[] samples) 
        {       
                if( buffer == null || buffer.capacity() != samples.length )
                {
                        ByteBuffer byteBuffer = ByteBuffer.allocateDirect( samples.length * Float.SIZE / 8 );
                        byteBuffer.order(ByteOrder.nativeOrder());
                        buffer = byteBuffer.asFloatBuffer();
                }
                
                int readSamples = readSamples( handle, buffer, samples.length );
                if( readSamples == 0 )
                {
                        closeFile( handle );
                        return 0;
                }
                
                buffer.position(0);
                buffer.get( samples );
                
                return samples.length;
        }
        
        public float getProgress()
        {
        	
        	return getProgress(handle);
        }
        
        /**
         * {@inheritDoc}
         */
        public int readSamples(short[][] samples, int sampleLength) 
        {       
                if( shortBuffer == null || shortBuffer[0].capacity() != sampleLength )
                {
                        ByteBuffer byteBufferL = ByteBuffer.allocateDirect(sampleLength * Short.SIZE / 8 );
                        byteBufferL.order(ByteOrder.nativeOrder());
                        ByteBuffer byteBufferR = ByteBuffer.allocateDirect(sampleLength * Short.SIZE / 8 );
                        byteBufferR.order(ByteOrder.nativeOrder());
                        shortBuffer = new ShortBuffer[2];
                        shortBuffer[0] = byteBufferL.asShortBuffer();
                        shortBuffer[1] = byteBufferR.asShortBuffer();
                }
                
                int readSamples = readSamples( handle, shortBuffer[0], shortBuffer[1], sampleLength);
                if( readSamples == 0 )
                {
                        closeFile( handle );
                        return 0;
                }
                
                shortBuffer[0].position(0);
                shortBuffer[1].position(0);
                shortBuffer[0].get( samples[0] );
                shortBuffer[1].get( samples[1] );
                
                return samples[0].length;
        }

        /**
         * {@inheritDoc}
         */
        public void dispose( )
        {
                closeFile(handle);
        }
        
        static{
        	
        	System.loadLibrary("audio-tools");
        }
}