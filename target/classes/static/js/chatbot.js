(function() {
  'use strict';

  console.log('🤖 Modern Travel Assistant Loading...');

  // Configuration
  const CONFIG = {
    typingSpeed: 50,
    responseDelay: 800,
    maxTypingDelay: 1500,
    soundEnabled: false,
    animations: true,
    theme: 'modern'
  };

  // State management
  let conversationHistory = [];
  let isTyping = false;
  let currentTheme = 'light';

  // Create chatbot button
  const createChatButton = () => {
    const button = document.createElement('button');
    button.id = 'travel-assistant-toggle';
    button.innerHTML = '✈️';
    button.className = 'travel-assistant-button';

    Object.assign(button.style, {
      position: 'fixed',
      bottom: '20px',
      right: '20px',
      zIndex: '9999',
      width: '60px',
      height: '60px',
      borderRadius: '50%',
      border: 'none',
      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
      color: 'white',
      fontSize: '24px',
      cursor: 'pointer',
      boxShadow: '0 4px 20px rgba(102, 126, 234, 0.3)',
      transition: 'all 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275)',
      animation: 'pulse 2s infinite',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center'
    });

    // Add hover effects
    button.addEventListener('mouseenter', () => {
      button.style.transform = 'scale(1.1)';
      button.style.boxShadow = '0 8px 30px rgba(102, 126, 234, 0.4)';
    });

    button.addEventListener('mouseleave', () => {
      button.style.transform = 'scale(1)';
      button.style.boxShadow = '0 4px 20px rgba(102, 126, 234, 0.3)';
    });

    return button;
  };

  // Create chat interface
  const createChatInterface = () => {
    const container = document.createElement('div');
    container.id = 'travel-assistant-container';
    container.className = 'travel-assistant-container';

    Object.assign(container.style, {
      position: 'fixed',
      bottom: '100px',
      right: '20px',
      width: '380px',
      maxWidth: '90vw',
      height: '600px',
      background: 'white',
      borderRadius: '20px',
      boxShadow: '0 20px 60px rgba(0, 0, 0, 0.15)',
      display: 'none',
      flexDirection: 'column',
      overflow: 'hidden',
      fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif',
      zIndex: '9998',
      border: '1px solid rgba(255, 255, 255, 0.2)',
      backdropFilter: 'blur(10px)'
    });

    // Header
    const header = document.createElement('div');
    header.className = 'chat-header';
    header.innerHTML = `
      <div style="display: flex; align-items: center; gap: 12px; padding: 16px 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border-radius: 20px 20px 0 0;">
        <div style="width: 40px; height: 40px; background: rgba(255, 255, 255, 0.2); border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 18px;">✈️</div>
        <div>
          <div style="font-weight: 600; font-size: 16px; margin-bottom: 2px;">Travel Assistant</div>
          <div style="font-size: 12px; opacity: 0.9; display: flex; align-items: center; gap: 4px;">
            <span style="width: 8px; height: 8px; background: #4ade80; border-radius: 50%;"></span>
            Online • Ready to help
          </div>
        </div>
        <button id="chat-minimize" style="margin-left: auto; background: transparent; border: none; color: white; font-size: 20px; cursor: pointer; padding: 4px; border-radius: 50%; transition: background 0.2s;" onmouseover="this.style.background='rgba(255,255,255,0.1)'" onmouseout="this.style.background='transparent'">─</button>
        <button id="chat-close" style="background: transparent; border: none; color: white; font-size: 18px; cursor: pointer; padding: 4px; border-radius: 50%; transition: background 0.2s;" onmouseover="this.style.background='rgba(255,255,255,0.1)'" onmouseout="this.style.background='transparent'">×</button>
      </div>
    `;

    // Messages container
    const messagesContainer = document.createElement('div');
    messagesContainer.id = 'chat-messages';
    messagesContainer.className = 'chat-messages';
    Object.assign(messagesContainer.style, {
      flex: '1',
      padding: '20px',
      overflowY: 'auto',
      background: '#f8fafc',
      display: 'flex',
      flexDirection: 'column',
      gap: '12px',
      scrollBehavior: 'smooth'
    });

    // Welcome message
    const welcomeMessage = createMessageElement(
      "Hi there! 👋 I'm your Travel Assistant! I'm here to help you plan your perfect trip. What can I assist you with today?",
      'bot',
      true
    );
    messagesContainer.appendChild(welcomeMessage);

    // Input area
    const inputArea = document.createElement('div');
    inputArea.className = 'chat-input-area';
    inputArea.innerHTML = `
      <div style="padding: 16px 20px; background: white; border-top: 1px solid #e2e8f0;">
        <div id="quick-replies" style="display: flex; gap: 8px; margin-bottom: 12px; flex-wrap: wrap;"></div>
        <form id="chat-form" style="display: flex; gap: 12px; align-items: flex-end;">
          <div style="flex: 1; position: relative;">
            <textarea id="chat-input" placeholder="Ask me anything about travel..." style="width: 100%; min-height: 44px; max-height: 120px; padding: 12px 16px; border: 2px solid #e2e8f0; border-radius: 22px; font-size: 14px; font-family: inherit; resize: none; outline: none; transition: border-color 0.2s; background: #f8fafc;" onfocus="this.style.borderColor='#667eea'" onblur="this.style.borderColor='#e2e8f0'"></textarea>
            <button type="button" id="emoji-btn" style="position: absolute; right: 12px; bottom: 12px; background: transparent; border: none; font-size: 18px; cursor: pointer; opacity: 0.6; transition: opacity 0.2s;" onmouseover="this.style.opacity='1'" onmouseout="this.style.opacity='0.6'">😊</button>
          </div>
          <button type="submit" style="width: 44px; height: 44px; border-radius: 50%; border: none; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; cursor: pointer; transition: all 0.2s; display: flex; align-items: center; justify-content: center; font-size: 16px;" onmouseover="this.style.transform='scale(1.05)'" onmouseout="this.style.transform='scale(1)'"><span id="send-icon">📤</span></button>
        </form>
      </div>
    `;

    container.appendChild(header);
    container.appendChild(messagesContainer);
    container.appendChild(inputArea);

    return container;
  };

  // Create message element
  const createMessageElement = (text, sender, isInitial = false) => {
    const messageWrapper = document.createElement('div');
    messageWrapper.className = `message-wrapper ${sender}`;
    messageWrapper.style.display = 'flex';
    messageWrapper.style.justifyContent = sender === 'user' ? 'flex-end' : 'flex-start';
    messageWrapper.style.marginBottom = '8px';

    const message = document.createElement('div');
    message.className = `message ${sender}`;
    message.style.maxWidth = '85%';
    message.style.padding = '12px 16px';
    message.style.borderRadius = sender === 'user' ? '18px 18px 4px 18px' : '18px 18px 18px 4px';
    message.style.background = sender === 'user' ? 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' : 'white';
    message.style.color = sender === 'user' ? 'white' : '#334155';
    message.style.border = sender === 'user' ? 'none' : '1px solid #e2e8f0';
    message.style.boxShadow = sender === 'user' ? '0 2px 12px rgba(102, 126, 234, 0.3)' : '0 2px 8px rgba(0, 0, 0, 0.08)';
    message.style.fontSize = '14px';
    message.style.lineHeight = '1.5';
    message.style.wordWrap = 'break-word';
    message.style.position = 'relative';

    // Add timestamp
    const timestamp = document.createElement('div');
    timestamp.className = 'message-timestamp';
    timestamp.style.fontSize = '11px';
    timestamp.style.opacity = '0.7';
    timestamp.style.marginTop = '4px';
    timestamp.style.textAlign = sender === 'user' ? 'right' : 'left';
    timestamp.textContent = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

    // Add avatar for bot messages
    if (sender === 'bot' && !isInitial) {
      const avatar = document.createElement('div');
      avatar.style.width = '24px';
      avatar.style.height = '24px';
      avatar.style.borderRadius = '50%';
      avatar.style.background = 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)';
      avatar.style.display = 'flex';
      avatar.style.alignItems = 'center';
      avatar.style.justifyContent = 'center';
      avatar.style.fontSize = '12px';
      avatar.style.marginRight = '8px';
      avatar.style.flexShrink = '0';
      avatar.textContent = '🤖';
      messageWrapper.appendChild(avatar);
    }

    // Add typing animation for bot messages
    if (sender === 'bot') {
      message.style.position = 'relative';
      message.style.overflow = 'hidden';
    }

    messageWrapper.appendChild(message);
    if (!isInitial) messageWrapper.appendChild(timestamp);

    // Animate message entrance
    setTimeout(() => {
      message.style.opacity = '0';
      message.style.transform = 'translateY(20px)';
      message.style.transition = 'all 0.3s ease';
      setTimeout(() => {
        message.style.opacity = '1';
        message.style.transform = 'translateY(0)';
      }, 50);
    }, 10);

    return messageWrapper;
  };

  // Add message to chat
  const addMessage = (text, sender, animate = true) => {
    const messagesContainer = document.getElementById('chat-messages');
    if (!messagesContainer) return;

    const messageElement = createMessageElement(text, sender);
    const messageText = messageElement.querySelector('.message');

    if (animate && sender === 'bot') {
      // Type out message character by character
      messageText.textContent = '';
      let index = 0;
      const typeText = () => {
        if (index < text.length) {
          messageText.textContent += text[index];
          index++;
          setTimeout(typeText, CONFIG.typingSpeed);
        }
      };
      setTimeout(typeText, 300);
    } else {
      messageText.textContent = text;
    }

    messagesContainer.appendChild(messageElement);
    messagesContainer.scrollTop = messagesContainer.scrollHeight;

    // Store in conversation history
    conversationHistory.push({ text, sender, timestamp: new Date() });

    return messageElement;
  };

  // Show typing indicator
  const showTypingIndicator = () => {
    if (isTyping) return;
    isTyping = true;

    const messagesContainer = document.getElementById('chat-messages');
    const typingWrapper = document.createElement('div');
    typingWrapper.id = 'typing-indicator';
    typingWrapper.className = 'typing-wrapper';
    typingWrapper.style.display = 'flex';
    typingWrapper.style.justifyContent = 'flex-start';
    typingWrapper.style.marginBottom = '8px';

    const typingBubble = document.createElement('div');
    typingBubble.style.padding = '12px 16px';
    typingBubble.style.background = 'white';
    typingBubble.style.border = '1px solid #e2e8f0';
    typingBubble.style.borderRadius = '18px';
    typingBubble.style.display = 'flex';
    typingBubble.style.alignItems = 'center';
    typingBubble.style.gap = '6px';
    typingBubble.style.boxShadow = '0 2px 8px rgba(0, 0, 0, 0.08)';

    typingBubble.innerHTML = `
      <div style="width: 6px; height: 6px; background: #94a3b8; border-radius: 50%; animation: typing-bounce 1.4s infinite;"></div>
      <div style="width: 6px; height: 6px; background: #94a3b8; border-radius: 50%; animation: typing-bounce 1.4s infinite 0.2s;"></div>
      <div style="width: 6px; height: 6px; background: #94a3b8; border-radius: 50%; animation: typing-bounce 1.4s infinite 0.4s;"></div>
    `;

    typingWrapper.appendChild(typingBubble);
    messagesContainer.appendChild(typingWrapper);
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
  };

  // Hide typing indicator
  const hideTypingIndicator = () => {
    const typingIndicator = document.getElementById('typing-indicator');
    if (typingIndicator) {
      typingIndicator.remove();
      isTyping = false;
    }
  };

  // Generate bot response
  const generateResponse = (userMessage) => {
    const message = userMessage.toLowerCase().trim();
    conversationHistory.push({ text: userMessage, sender: 'user', timestamp: new Date() });

    // Context-aware responses
    const lastMessages = conversationHistory.slice(-3);
    const context = lastMessages.map(m => m.text.toLowerCase()).join(' ');

    // Enhanced response logic
    if (message.includes('hello') || message.includes('hi') || message.includes('hey') || message.includes('greetings')) {
      return "Hello! 👋 I'm thrilled to help you plan your dream vacation! What destination are you dreaming about, or what type of travel experience are you looking for?";
    }

    if (message.includes('help') || message.includes('assist') || message.includes('support')) {
      return "I'm here to help! I can assist with:\n\n🏨 Hotel recommendations\n✈️ Flight information\n🎯 Travel packages\n📍 Destination guides\n💰 Price comparisons\n🗓️ Best travel times\n🛡️ Safety information\n📞 Contact details\n\nWhat specific area would you like help with?";
    }

    if (message.includes('package') || message.includes('tour') || message.includes('trip') || message.includes('travel')) {
      const destinations = ['Goa', 'Kerala', 'Rajasthan', 'Himachal Pradesh', 'Uttarakhand', 'Kashmir', 'Singapore', 'Dubai', 'Thailand', 'Maldives'];
      const randomDest = destinations[Math.floor(Math.random() * destinations.length)];
      return `Fantastic! We have incredible travel packages for every budget and preference! \n\n🌟 Popular destinations include ${randomDest}, Goa, Kerala, and international gems like Singapore and Dubai.\n\nWhat type of experience are you looking for? (Adventure, Relaxation, Cultural, Family, Solo, etc.)`;
    }

    if (message.includes('budget') || message.includes('price') || message.includes('cost') || message.includes('cheap') || message.includes('expensive')) {
      return `Great question about pricing! Our packages range from:\n\n💰 Budget: ₹8,999 - ₹25,999\n💎 Standard: ₹26,000 - ₹75,999\n🌟 Premium: ₹76,000 - ₹2,50,000+\n\nPrices vary based on destination, duration, and inclusions. International trips start from ₹35,999. Would you like a specific quote for your preferred destination?`;
    }

    if (message.includes('hotel') || message.includes('stay') || message.includes('accommodation') || message.includes('resort')) {
      return `I love helping with hotel recommendations! 🏨\n\nWe partner with top-rated hotels offering:\n⭐ 3-5 star properties\n🏖️ Beach resorts\n🏔️ Mountain retreats\n🏙️ City hotels\n\nWhat destination are you considering, and what's your preferred star rating and budget range?`;
    }

    if (message.includes('flight') || message.includes('fly') || message.includes('airline') || message.includes('ticket')) {
      return `Perfect! ✈️ I can help you find the best flight deals!\n\nPlease share:\n📍 Departure city\n🎯 Destination\n🗓️ Preferred travel dates\n👥 Number of travelers\n\nI can compare prices across multiple airlines and find you the best options!`;
    }

    if (message.includes('beach') || message.includes('sea') || message.includes('ocean') || message.includes('island')) {
      return `Beach paradise awaits! 🏖️\n\n🏝️ Top beach destinations:\n• Goa - Party & relaxation\n• Kerala - Backwaters & serenity\n• Andaman Islands - Pristine beaches\n• Maldives - Overwater luxury\n• Bali - Cultural beaches\n\nWhich type of beach experience calls to you?`;
    }

    if (message.includes('mountain') || message.includes('hill') || message.includes('trek') || message.includes('adventure')) {
      return `Adventure and mountains - my favorite! 🏔️\n\n🌄 Top mountain destinations:\n• Manali - Snow & adventure\n• Shimla - Colonial charm\n• Kashmir - Paradise on earth\n• Uttarakhand - Spiritual treks\n• Himachal Pradesh - Mixed experiences\n\nAre you looking for trekking, relaxation, or a mix of both?`;
    }

    if (message.includes('family') || message.includes('kids') || message.includes('children') || message.includes('group')) {
      return `Family travel is wonderful! 👨‍👩‍👧‍👦\n\nWe specialize in family-friendly packages with:\n🧒 Kid-friendly activities\n👨‍👩‍👧‍👦 Family accommodations\n🍽️ Family dining options\n🛡️ Safety-first approach\n\nHow many people in your group, and what ages are the children?`;
    }

    if (message.includes('solo') || message.includes('single') || message.includes('alone')) {
      return `Solo travel is empowering! 🌟\n\nWe have specially curated solo traveler packages with:\n🛡️ Safety-first planning\n👥 Group activities to meet people\n🏨 Safe, well-reviewed accommodations\n📍 Local experiences\n\nWhat destination interests you most for your solo adventure?`;
    }

    if (message.includes('honeymoon') || message.includes('romantic') || message.includes('couple') || message.includes('wedding')) {
      return `Congratulations on your special occasion! 💕✨\n\nRomantic destinations we love:\n🏖️ Maldives - Overwater bungalows\n🏔️ Switzerland - Alpine romance\n🏝️ Mauritius - Tropical paradise\n🍷 Tuscany - Wine country charm\n\nWhat's your dream honeymoon destination?`;
    }

    if (message.includes('food') || message.includes('cuisine') || message.includes('eat') || message.includes('restaurant')) {
      return `Food lovers unite! 🍽️\n\n🍛 Culinary destinations:\n• Delhi - Street food heaven\n• Goa - Seafood paradise\n• Kerala - Spice trail\n• Rajasthan - Royal cuisine\n• Mumbai - Diverse flavors\n\nAre you interested in food tours, cooking classes, or restaurant recommendations?`;
    }

    if (message.includes('culture') || message.includes('heritage') || message.includes('history') || message.includes('traditional')) {
      return `Cultural immersion is magical! 🕌\n\n🏛️ Heritage destinations:\n• Rajasthan - Royal palaces\n• Golden Triangle - Historical sites\n• South India - Ancient temples\n• Varanasi - Spiritual heritage\n• Hampi - Ancient ruins\n\nWhich aspect of Indian culture interests you most?`;
    }

    if (message.includes('wildlife') || message.includes('safari') || message.includes('animal') || message.includes('jungle')) {
      return `Wildlife adventures await! 🦌\n\n🦁 Top safari destinations:\n• Ranthambore - Tiger spotting\n• Jim Corbett - Elephant safaris\n• Bandhavgarh - Rich biodiversity\n• Kanha - Barasingha deer\n• Periyar - Elephant encounters\n\nBest time for wildlife spotting is October to March. Which park interests you?`;
    }

    if (message.includes('festival') || message.includes('celebration') || message.includes('event') || message.includes('holi') || message.includes('diwali')) {
      return `Indian festivals are spectacular! 🎉\n\n🌈 Festival experiences:\n• Holi - Colors and joy\n• Diwali - Lights and sweets\n• Pushkar Fair - Camel festival\n• Durga Puja - Cultural celebration\n• Local village festivals\n\nPlanning around a specific festival? Let me know which one!`;
    }

    if (message.includes('covid') || message.includes('safety') || message.includes('health') || message.includes('protocol')) {
      return `Your safety is our top priority! 🛡️\n\n✅ Safety measures include:\n• Sanitized vehicles\n• Certified partner hotels\n• Small group sizes\n• Health monitoring\n• Flexible cancellation\n• Insurance options\n\nAll our partners follow government guidelines. Would you like more details?`;
    }

    if (message.includes('booking') || message.includes('book') || message.includes('reserve') || message.includes('confirm')) {
      return `Ready to book? That's exciting! 🎉\n\n📋 Booking process:\n1. Choose your package\n2. Select dates & travelers\n3. Customize inclusions\n4. Secure payment\n5. Get confirmation\n\nI can guide you through each step or connect you with our booking team. What would you like to book?`;
    }

    if (message.includes('cancel') || message.includes('refund') || message.includes('change')) {
      return `I understand plans can change! 🔄\n\n📋 Our policies:\n✅ Free cancellation up to 7 days\n✅ 50% refund 8-14 days before\n✅ Date changes allowed\n✅ Full refund for cancellations due to COVID\n\nFor cancellations or changes, please contact our support team directly.`;
    }

    if (message.includes('payment') || message.includes('pay') || message.includes('card') || message.includes('upi')) {
      return `Payment made easy! 💳\n\n💰 Payment options:\n• Credit/Debit cards\n• UPI payments\n• Net banking\n• EMI options\n• Cryptocurrency\n\n🔒 All payments are secure and encrypted. We also offer installment plans for select packages.`;
    }

    if (message.includes('contact') || message.includes('phone') || message.includes('email') || message.includes('reach')) {
      return `Get in touch with us! 📞\n\n📧 Email: support@happyghumakkads.com\n📞 Phone: +91-98765-43210\n💬 Live Chat: Available 24/7\n📍 Office: Visit our branch\n\nOur support team is available round the clock to assist you!`;
    }

    if (message.includes('weather') || message.includes('climate') || message.includes('season') || message.includes('temperature')) {
      return `Perfect timing question! 🌤️\n\n🗓️ Best travel seasons:\n• October-March: Most destinations\n• April-June: Hill stations\n• July-September: Limited due to monsoon\n• December-February: Winter destinations\n\nWhat's your preferred travel month and destination?`;
    }

    if (message.includes('visa') || message.includes('passport') || message.includes('documentation')) {
      return `Visa and documentation assistance! 📋\n\n🌍 For international travel:\n• We provide visa guidance\n• Document checklist\n• Application assistance\n• Travel insurance\n• Forex services\n\nWhich country are you planning to visit?`;
    }

    if (message.includes('thanks') || message.includes('thank you') || message.includes('appreciate')) {
      return "You're so welcome! 😊 I'm glad I could help! If you have any more questions about your travel plans, I'm here 24/7. Safe travels! ✈️";
    }

    if (message.includes('bye') || message.includes('goodbye') || message.includes('see you')) {
      return "Goodbye! 👋 It was wonderful chatting with you! Remember, I'm always here if you need travel advice. Have an amazing journey! 🌍";
    }

    // Default response with context awareness
    const defaultResponses = [
      "That's an interesting question! 🤔 Could you tell me more about what you're looking for?",
      "I'd love to help with that! Can you provide a few more details?",
      "Great question! What specific aspect would you like to know more about?",
      "I'm here to help! Could you elaborate on what you're planning?"
    ];

    return defaultResponses[Math.floor(Math.random() * defaultResponses.length)];
  };

  // Initialize chatbot
  const initChatbot = () => {
    console.log('🤖 Initializing Modern Travel Assistant...');

    try {
      // Create elements
      const button = createChatButton();
      const container = createChatInterface();

      // Add to DOM
      document.body.appendChild(button);
      document.body.appendChild(container);

      console.log('🤖 Travel Assistant elements added successfully');

      // Event listeners
      button.addEventListener('click', toggleChat);
      document.getElementById('chat-close').addEventListener('click', closeChat);
      document.getElementById('chat-minimize').addEventListener('click', minimizeChat);
      document.getElementById('chat-form').addEventListener('submit', handleSubmit);
      document.getElementById('chat-input').addEventListener('keydown', handleKeydown);
      document.getElementById('emoji-btn').addEventListener('click', toggleEmojiPicker);

      // Auto-resize textarea
      document.getElementById('chat-input').addEventListener('input', autoResizeTextarea);

      // Window events
      window.addEventListener('resize', handleResize);
      window.addEventListener('scroll', handleResize);

      console.log('🤖 Travel Assistant initialized successfully');

    } catch (error) {
      console.error('🤖 Error initializing Travel Assistant:', error);
    }
  };

  // Toggle chat visibility
  const toggleChat = () => {
    const container = document.getElementById('travel-assistant-container');
    const button = document.getElementById('travel-assistant-toggle');

    if (container.style.display === 'none' || !container.style.display) {
      container.style.display = 'flex';
      button.style.display = 'none';
      document.getElementById('chat-input').focus();

      // Animate entrance
      setTimeout(() => {
        container.style.opacity = '0';
        container.style.transform = 'translateY(20px) scale(0.95)';
        container.style.transition = 'all 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275)';
        setTimeout(() => {
          container.style.opacity = '1';
          container.style.transform = 'translateY(0) scale(1)';
        }, 50);
      }, 10);

    } else {
      closeChat();
    }
  };

  // Close chat
  const closeChat = () => {
    const container = document.getElementById('travel-assistant-container');
    const button = document.getElementById('travel-assistant-toggle');

    container.style.opacity = '0';
    container.style.transform = 'translateY(20px) scale(0.95)';
    setTimeout(() => {
      container.style.display = 'none';
      button.style.display = 'flex';
    }, 300);
  };

  // Minimize chat
  const minimizeChat = () => {
    const container = document.getElementById('travel-assistant-container');
    const messages = document.getElementById('chat-messages');
    const inputArea = document.querySelector('.chat-input-area');

    if (container.classList.contains('minimized')) {
      container.classList.remove('minimized');
      messages.style.display = 'flex';
      inputArea.style.display = 'block';
      document.getElementById('chat-minimize').textContent = '─';
    } else {
      container.classList.add('minimized');
      messages.style.display = 'none';
      inputArea.style.display = 'none';
      document.getElementById('chat-minimize').textContent = '+';
    }
  };

  // Handle form submission
  const handleSubmit = (e) => {
    e.preventDefault();
    const input = document.getElementById('chat-input');
    const message = input.value.trim();

    if (!message) return;

    // Add user message
    addMessage(message, 'user', true);
    input.value = '';
    autoResizeTextarea();

    // Show typing indicator
    showTypingIndicator();

    // Generate and show bot response
    setTimeout(() => {
      hideTypingIndicator();
      const response = generateResponse(message);
      addMessage(response, 'bot', true);

      // Add quick replies based on context
      addQuickReplies(message);
    }, CONFIG.responseDelay + Math.random() * CONFIG.maxTypingDelay);
  };

  // Handle keyboard input
  const handleKeydown = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSubmit(e);
    }
  };

  // Auto-resize textarea
  const autoResizeTextarea = () => {
    const textarea = document.getElementById('chat-input');
    textarea.style.height = 'auto';
    textarea.style.height = Math.min(textarea.scrollHeight, 120) + 'px';
  };

  // Add contextual quick replies
  const addQuickReplies = (userMessage) => {
    const quickRepliesContainer = document.getElementById('quick-replies');
    quickRepliesContainer.innerHTML = '';

    const message = userMessage.toLowerCase();
    let replies = [];

    if (message.includes('package') || message.includes('tour') || message.includes('trip')) {
      replies = ['🏨 Hotels', '✈️ Flights', '💰 Budget options', '📅 Best time to visit'];
    } else if (message.includes('hotel') || message.includes('stay')) {
      replies = ['⭐ 5-Star hotels', '💰 Budget hotels', '🏖️ Beach resorts', '🏔️ Mountain resorts'];
    } else if (message.includes('beach') || message.includes('sea')) {
      replies = ['🏝️ Goa beaches', '🏖️ Kerala backwaters', '🌊 Andaman Islands', '🏝️ Maldives'];
    } else if (message.includes('mountain') || message.includes('hill')) {
      replies = ['🏔️ Manali', '🏔️ Shimla', '🏔️ Kashmir', '🏔️ Uttarakhand'];
    } else {
      replies = ['🏨 Hotels', '✈️ Flights', '🎯 Packages', '💰 Pricing'];
    }

    replies.forEach(reply => {
      const button = document.createElement('button');
      button.textContent = reply;
      button.className = 'quick-reply-btn';
      button.style.cssText = `
        background: #f1f5f9;
        border: 1px solid #e2e8f0;
        border-radius: 20px;
        padding: 6px 12px;
        font-size: 12px;
        cursor: pointer;
        transition: all 0.2s;
        margin: 2px;
        white-space: nowrap;
      `;

      button.addEventListener('mouseover', () => {
        button.style.background = '#e2e8f0';
        button.style.transform = 'translateY(-1px)';
      });

      button.addEventListener('mouseout', () => {
        button.style.background = '#f1f5f9';
        button.style.transform = 'translateY(0)';
      });

      button.addEventListener('click', () => {
        document.getElementById('chat-input').value = reply;
        handleSubmit(new Event('submit'));
      });

      quickRepliesContainer.appendChild(button);
    });
  };

  // Handle window resize
  const handleResize = () => {
    const container = document.getElementById('travel-assistant-container');
    if (container && container.style.display !== 'none') {
      adjustChatPosition();
    }
  };

  // Adjust chat position
  const adjustChatPosition = () => {
    const container = document.getElementById('travel-assistant-container');
    const button = document.getElementById('travel-assistant-toggle');

    if (!container || container.style.display === 'none') return;

    const viewportHeight = window.innerHeight;
    const containerHeight = container.offsetHeight;
    const buttonHeight = button.offsetHeight;
    const margin = 20;

    if (containerHeight > viewportHeight - buttonHeight - margin - 100) {
      container.style.bottom = (buttonHeight + margin + 10) + 'px';
      container.style.top = margin + 'px';
      container.style.height = Math.min(600, viewportHeight - buttonHeight - margin - margin - 20) + 'px';
    } else {
      container.style.bottom = (buttonHeight + margin) + 'px';
      container.style.top = 'auto';
      container.style.height = '600px';
    }
  };

  // Emoji picker (basic)
  const toggleEmojiPicker = () => {
    const input = document.getElementById('chat-input');
    const emojis = ['😊', '👍', '❤️', '🎉', '✈️', '🏖️', '🏔️', '🍽️', '🗺️', '📸'];
    const randomEmoji = emojis[Math.floor(Math.random() * emojis.length)];
    input.value += randomEmoji;
    input.focus();
  };

  // Add CSS animations
  const addStyles = () => {
    const style = document.createElement('style');
    style.textContent = `
      @keyframes pulse {
        0%, 100% { transform: scale(1); }
        50% { transform: scale(1.05); }
      }

      @keyframes typing-bounce {
        0%, 60%, 100% { transform: translateY(0); opacity: 1; }
        30% { transform: translateY(-8px); opacity: 0.6; }
      }

      .travel-assistant-button {
        animation: pulse 2s infinite;
      }

      .chat-messages::-webkit-scrollbar {
        width: 4px;
      }

      .chat-messages::-webkit-scrollbar-track {
        background: #f1f5f9;
      }

      .chat-messages::-webkit-scrollbar-thumb {
        background: #cbd5e1;
        border-radius: 2px;
      }

      .chat-messages::-webkit-scrollbar-thumb:hover {
        background: #94a3b8;
      }

      .quick-reply-btn:hover {
        background: #e2e8f0 !important;
        transform: translateY(-1px) !important;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      }

      .message {
        animation: slideIn 0.3s ease;
      }

      @keyframes slideIn {
        from { opacity: 0; transform: translateY(20px); }
        to { opacity: 1; transform: translateY(0); }
      }

      @media (max-width: 480px) {
        .travel-assistant-container {
          width: 100vw !important;
          height: 100vh !important;
          right: 0 !important;
          bottom: 0 !important;
          border-radius: 0 !important;
        }

        .chat-header {
          padding: 12px 16px !important;
        }

        .chat-messages {
          padding: 16px !important;
        }

        .chat-input-area {
          padding: 12px 16px !important;
        }
      }
    `;
    document.head.appendChild(style);
  };

  // Initialize when DOM is ready
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
      addStyles();
      initChatbot();
    });
  } else {
    addStyles();
    initChatbot();
  }

})();
